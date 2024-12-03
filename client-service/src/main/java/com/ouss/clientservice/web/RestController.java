package com.ouss.clientservice.web;

import com.ouss.clientservice.entites.Client;
import com.ouss.clientservice.repository.ClientRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
    @Autowired
    ClientRepository clientRepository;





    @GetMapping("/clients")

    public List<Client> getAll(Pageable pageable){
        return clientRepository.findAll(pageable).getContent();
    }
    @GetMapping("/clients/{id}")
    public Client clientbyid(@PathVariable Integer id){
        return clientRepository.findById(id).orElseThrow(()-> new RuntimeException("Client not found"));
    }
    @PutMapping("/clients/{id}")
    public Client updateClient(@PathVariable Integer id,@RequestBody Client client){
        Client c = clientRepository.findById(id).orElseThrow(()-> new RuntimeException("Client not found"));
        if (client.getNom()!=null) c.setNom(client.getNom());
        if (client.getPrenom()!=null) c.setPrenom(client.getPrenom());
        if (client.getEmail()!=null) c.setEmail(client.getEmail());
        if(client.getRoles()!=null) c.setRoles(client.getRoles());
        if(client.getPassword()!=null) c.setPassword(client.getPassword());





        return clientRepository.save(c);
    }
    @PostMapping("/clients")
    public Client saveClient(@RequestBody Client client){
        Client c = new Client();
        c.setNom(client.getNom());
        c.setPrenom(client.getPrenom());
        c.setEmail(client.getEmail());
        c.setRoles(client.getRoles());
        c.setPassword(client.getPassword());
        return clientRepository.save(c);
    }
    @DeleteMapping("/clients/{id}")
    public void deleteClient(@PathVariable Integer id){
        clientRepository.deleteById(id);

    }

    @GetMapping("/authenticate")
    public Authentication authentication(Authentication authentication){
        return authentication;
    }
}
