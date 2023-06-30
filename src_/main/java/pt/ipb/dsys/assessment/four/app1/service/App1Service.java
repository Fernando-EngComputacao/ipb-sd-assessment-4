package pt.ipb.dsys.assessment.four.app1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;
import pt.ipb.dsys.assessment.four.common.UpdateService;
import pt.ipb.dsys.assessment.four.model.CriticalResource;
import pt.ipb.dsys.assessment.four.model.Token;

import javax.jms.TextMessage;

@org.springframework.stereotype.Service
public class App1Service {
    private static final Logger logger = LoggerFactory.getLogger(pt.ipb.dsys.assessment.four.app2.service.App2Service.class);
    private final RestTemplate template;
    private final ObjectMapper objectMapper;

    private final UpdateService updateService;
    private final JmsTemplate jmsTemplate;

    public App1Service(JmsTemplate jmsTemplate, ObjectMapper objectMapper, UpdateService updateService) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
        this.updateService = updateService;
        this.template = new RestTemplate();
    }

    @JmsListener(destination = "queue-1")
    public void handle(TextMessage textMessage) throws Exception {

        String jsonText = textMessage.getText();
        Token tokenValue = objectMapper.readValue(jsonText, Token.class);

        logger.info("Token recebido {}", tokenValue.getToken());

        if(updateService.shouldUpdate()) {
            CriticalResource resource = template.getForObject("http://127.0.0.1:8989/api/resource", CriticalResource.class);
            assert resource != null;
            double multiplyNumber = resource.getValue() / 1.1;
            resource.setValue(multiplyNumber);
            template.put("http://127.0.0.1:8989/api/resource/" + resource.getValue(), resource);
            logger.info("A enviar {} para o servidor!", resource.getValue());
        }

        send(tokenValue,"queue-2");
        logger.info("Para o próximo cenário!");
    }
    public void send(Object obj, String queue) {
        try {
            String jsonText = objectMapper.writeValueAsString(obj);
            jmsTemplate.convertAndSend(queue, jsonText);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
