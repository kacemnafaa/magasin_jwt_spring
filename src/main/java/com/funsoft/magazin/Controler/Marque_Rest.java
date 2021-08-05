package com.funsoft.magazin.Controler;

import com.funsoft.magazin.Repository.MarqueRepository;
import com.funsoft.magazin.exception.MessageException;
import com.funsoft.magazin.model.Marque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Marque_Rest {
    @Autowired
    MarqueRepository agent;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/marques")//RESTmaping(value="/",methode=Get)
    public Marque add_marque(@RequestBody Marque marque){
        return agent.save(marque);
    }@PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/marques")
    public List<Marque>list_marques(){
        return agent.findAll();
    }
    //ResponseEntity<?> manich sur chnwa bech yarja3
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("/marques/{id}")
    public ResponseEntity<?> get_marque(@PathVariable("id") Long id) throws MessageException{
        Marque m =agent.findById(id).orElseThrow(()->
        new MessageException("acune marque ne correspend à lidentifiant "+id));
        return ResponseEntity.ok().body(m);
    }@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/marques/{id}")
    public Map<String,Boolean> delete_marque(@PathVariable("id") Long id)throws MessageException{
        Marque m =agent.findById(id).orElseThrow(()->
                new MessageException("acune marque ne correspend à l'identifiant "+id));
        agent.delete(m);
        Map<String,Boolean> res=new HashMap<>();
        res.put("deleteId",Boolean.TRUE);
        return res;

    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping(value = "/marques/{id}")
    public Marque update_marque(@PathVariable("id") Long id,@RequestBody Marque marque) throws MessageException{
        Marque m =agent.findById(id).orElseThrow(()->
                new MessageException("acune marque ne correspend à l'identifiant "+id));

        m.setNom_marque(marque.getNom_marque());
        return agent.save((m));

    }
}
