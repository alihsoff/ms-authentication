package az.covid.msauthentication.service;


import az.covid.msauthentication.model.dto.MailDTO;

public interface EmailService {

    void sendToQueue(MailDTO mailDTO);
}
