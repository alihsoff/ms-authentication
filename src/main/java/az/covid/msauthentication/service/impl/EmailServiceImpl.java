package az.covid.msauthentication.service.impl;

import az.covid.msauthentication.model.dto.MailDTO;
import az.covid.msauthentication.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Source.class)
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final Source source;

    @Override
    public void sendToQueue(MailDTO mailDTO) {
        source.output().send(MessageBuilder.withPayload(mailDTO).build());
    }
}
