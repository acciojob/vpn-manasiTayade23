package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        User user = userRepository2.findById(userId).get();
        if(user.getMaskedIp()!=null){
           throw new Exception("Already Connected");
       }else if(countryName.equalsIgnoreCase(user.getOriginalCountry().getCountryName().toString())){
           return user;
       }else{
           if(user.getServiceProviderList() == null){
               throw new Exception("Unable To Connect");
           }
           List<ServiceProvider> serviceProviderList=user.getServiceProviderList();
           int min=Integer.MIN_VALUE;
           Country country=null;
           ServiceProvider serviceProvider=null;

           for(ServiceProvider sp:serviceProviderList){
               List<Country> countryList=sp.getCountryList();
               for(Country c:countryList){
                   if(countryName.equalsIgnoreCase(country.getCountryName().toString()) && min > sp.getId()){
                       min=sp.getId();
                       country=c;
                       serviceProvider=sp;

                   }
               }
           }
           if(serviceProvider != null){
               Connection connection=new Connection();
               connection.setServiceProvider(serviceProvider);
               connection.setUser(user);

               String countryCode=country.getCode();
               int providerId=serviceProvider.getId();
               String masked=countryCode+providerId+userId;

               user.setConnected(true);
               user.setMaskedIp(masked);
               user.getConnectionList().add(connection);

               serviceProvider.getConnectionList().add(connection);
               userRepository2.save(user);
               serviceProviderRepository2.save(serviceProvider);

               return user;
           }else{
               throw new Exception("Unable To Connect");
           }
       }

    }
    @Override
    public User disconnect(int userId) throws Exception {
        User user =userRepository2.findById(userId).get();
        if(!user.getConnected()){
            throw new Exception("Disconnected");
        }
        user.setConnected(false);
        user.setMaskedIp(null);
        userRepository2.save(user);

        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User sender=userRepository2.findById(senderId).get();
        User receiver=userRepository2.findById(receiverId).get();

        if(receiver.getMaskedIp()!=null){
            String IP=receiver.getMaskedIp();
            //chop
            String code=IP.substring(0,3);

            if(code.equals(sender.getOriginalCountry().getCode())) return sender;
            else{
                String countryName="";
                if(code.equals(CountryName.IND.toCode())){
                    countryName=CountryName.IND.toCode();
                }
                if(code.equals(CountryName.USA.toCode())){
                    countryName=CountryName.USA.toCode();
                }
                if(code.equals(CountryName.JPN.toCode())){
                    countryName=CountryName.JPN.toCode();
                }
                if(code.equals(CountryName.AUS.toCode())){
                    countryName=CountryName.AUS.toCode();
                }
                if(code.equals(CountryName.CHI.toCode())){
                    countryName=CountryName.CHI.toCode();
                }
                try{
                    User senderUpdated=connect(senderId,countryName);
                    return senderUpdated;
                }catch (Exception e){
                    throw new Exception("Error");
                }
            }
        }else{
            if(receiver.getOriginalCountry().equals(sender.getOriginalCountry())) return sender;
            else{
                String countryName=receiver.getOriginalCountry().getCountryName().toString();
                try{
                    User updateSender=connect(senderId,countryName);
                    return updateSender;
                }catch (Exception e){
                    throw new Exception("Error");
                }
            }
        }
    }
}
