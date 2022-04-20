package ch.fhnw.digi.demo;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsHeaders;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

@Component
public class Publisher {

    @Autowired
    private SimpleUi simpleui;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    AckSender ackSender;

    private static Queue<GreeterMessage> messages = new LinkedList<>();


    @PostConstruct
    void run() {
        if (messages.size() > 0) {
            GreeterMessage c = messages.poll();

            jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());

            final String correlationID = c.getName();

            final String value = c.getValue();
            final String status = (Math.random() > 0.5 ? "accepted" : "declined" ) + " by Person 2" ;
            final String queue = "validQ";


            System.out.print("The risk is " + value + " and the queue is " + queue + ".");

            // publish a new GreeterMessage to the channel "greetRequests"
            jmsTemplate.convertAndSend(queue, new GreeterMessage(correlationID, value, status), m -> {
                m.setStringProperty("someHeaderField", "someImportantValue");
                m.setJMSCorrelationID(correlationID);
                return m;
            });

            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
            }
                // just so we see something in our gui
            simpleui.setMessage("published Message with id: " + correlationID + "\n\nThe risk is " + value +
                    " and the queue is " + queue + ".\n\nStatus is: " + status);
        }
    }

    // used to convert our java message object into a JSON String that can be sent
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }


    @JmsListener(destination = "personTwoQ", containerFactory = "myFactory")
    public void receiveMessage(GreeterMessage c, @Header(JmsHeaders.CORRELATION_ID) String correlationId) {

        simpleui.setMessage("received personOneQ request " + c.getName() + " with id: " + correlationId);

        messages.add(c);
        run();
        try {
            Thread.sleep(1000);
            //ackSender.sendMessage(new String("Acknowledgement from Receiver for id:" + correlationId));
        } catch (InterruptedException e) {
        }

    }


    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message
        // converter
        configurer.configure(factory, connectionFactory);

        simpleui.setMessage("connection factory created");

        // You could still override some of Boot's default if necessary.
        return factory;
    }


    class AckSender {

        public void sendMessage(String message) {
            // publish ack to the channel "ackQueue"
            jmsTemplate.convertAndSend("ackQueue", message);
        }

        @Bean // Serialize message content to json/from using TextMessage
        public MessageConverter jacksonJmsMessageConverter() {
            MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
            converter.setTargetType(MessageType.TEXT);
            converter.setTypeIdPropertyName("_type");
            return converter;
        }

    }

}