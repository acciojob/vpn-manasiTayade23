package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        if(countryName.equalsIgnoreCase("aus")||countryName.equalsIgnoreCase("ind")||countryName.equalsIgnoreCase("jpn")||
        countryName.equalsIgnoreCase("chi")||countryName.equalsIgnoreCase("usa")){
            //create obj
            User user=new User();
            //set attributes
            user.setPassword(password);
            user.setUsername(username);

            Country country=new Country();

            if(countryName.equalsIgnoreCase("ind")){
                country.setCode(CountryName.IND.toCode());
                country.setCountryName(CountryName.IND);
            }

            if(countryName.equalsIgnoreCase("jpn")){
                country.setCountryName(CountryName.JPN);
                country.setCode(CountryName.JPN.toCode());
            }

            if(countryName.equalsIgnoreCase("chi")){
                country.setCode(CountryName.CHI.toCode());
                country.setCountryName(CountryName.CHI);
            }

            if(countryName.equalsIgnoreCase("aus")){
                country.setCountryName(CountryName.AUS);
                country.setCode(CountryName.AUS.toCode());
            }
            if(countryName.equalsIgnoreCase("usa")){
                country.setCode(CountryName.USA.toCode());
                country.setCountryName(CountryName.USA);
            }
            //set attributes
            country.setUser(user);
            user.setConnected(false);
            user.setOriginalCountry(country);

            String IPAddress=country.getCode()+userRepository3.save(user).getId();
            user.setOriginalIp(IPAddress);

            userRepository3.save(user);
            return user;
        }else
            throw new Exception("Not Found");
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        User user=userRepository3.findById(userId).get();
        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();

        serviceProvider.getUserList().add(user);
        user.getUsers().add(serviceProvider);

        serviceProviderRepository3.save(serviceProvider);

        return user;
    }
}
