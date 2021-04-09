package com.yinkin.yinkinelderservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.amazonaws.services.ec2.model.Address;
import com.yinkin.yinkinelderservice.model.*;

@Controller
@RequestMapping({ "/account" })
// @RequestMapping("/api/account")
public class AccountController {
    private final AccountRepository accountRepository;
    private final EmployerRepository employerRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository, 
            EmployerRepository employerRepository){
        this.accountRepository = accountRepository;
        this.employerRepository = employerRepository;
    }

    @GetMapping("/profile")
    public String index(Model model) {
        //model.addAttribute("account", new Account());
        
        return "/user/profile";
    }

    @GetMapping
    public String register(Model model) {
	    model.addAttribute("account", new Account());
        return "register";
    }

    // @PostMapping("/register/new")
    // @ResponseStatus(HttpStatus.CREATED)
    // Account newEmployee(@RequestBody Account newAccount) {
        
    //   return accountRepository.save(newAccount);
    // }
    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public String registrationHandler( Account newAccount) {
        //Account anewAccount =
        //model.addAttribute("account", newAccount);
        if (accountRepository.findByEmail(newAccount.getEmail()) !=null){
             // account already exists.
        }else{
            accountRepository.save(newAccount);
        }
        
      return "redirect:/account/register/success";
    }
    @GetMapping("/register/success")
    //public String getSuccess() {
	public ResponseEntity<String> getSuccess() {
	    return new ResponseEntity<String>("Thank you for registering. <a href='/'>back</a>", HttpStatus.OK);
        //return "redirect:/service";
    }

    @GetMapping("/list")
    //public String getSuccess() {
	public String accountListView(Model model) {
        List<Account> accountList = accountRepository.findAll();

        model.addAttribute("accounts", accountList);
	    return"/account/accountlist";
        //return "redirect:/service";
    }

    @PutMapping("/user/{id}")
    Account replaceEmployee(@RequestBody Account newAccount, @PathVariable Long id) {
        return newAccount;
    }

    // @GetMapping("/employer/{name}")
    // public String replaceEmployee(Model model,@PathVariable String name) {
    //     List<Employer> employerList = employerRepository.findByLastName(name);
    //     if(!name.isEmpty()){
    //     model.addAttribute("name", name);
    //     model.addAttribute("employers", employerList);

    //     return "/account/employerList";
    //     }else{
    //         return "access-denied";
    //     }
    // }
    @GetMapping("/employer/register/{name}")
    public String registerEmployee(Model model,@PathVariable String name) {
        //List<Employer> employerList = employerRepository.findByLastName(name);
        if (employerRepository.findByFirstName(name) !=null){
            // account already exists.
       }else{
           List<Account> accountList  = accountRepository.findByName(name);
           String userAdress = "";
           for (Account account : accountList) {
            userAdress = account.getAddress();
           }
           Employer newEmployee = new Employer(name, "",  userAdress );
           employerRepository.save(newEmployee);
           model.addAttribute("accounts", accountList);
           return "/account/employerRegistered";
       }
        model.addAttribute("name", name);
        

       return "/account/accessdenied";
    }


    //@DeleteMapping("/employees/{id}")
    @DeleteMapping("/resetdb")
    void deleteEmployee(@PathVariable Long id) {
        accountRepository.deleteAll();;
    }
}
