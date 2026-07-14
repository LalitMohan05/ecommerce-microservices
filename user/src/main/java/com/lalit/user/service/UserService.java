package com.lalit.user.service;

import com.lalit.user.dto.AddressDTO;
import com.lalit.user.dto.UserRequest;
import com.lalit.user.dto.UserResponse;
import com.lalit.user.entity.Address;
import com.lalit.user.entity.User;
import com.lalit.user.exceptions.UserNotFoundException;
import com.lalit.user.repository.UserRepo;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<UserResponse> fetchALlUser(){
        List<User> userList = userRepo.findAll();
        return userRepo.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public void addUser(UserRequest userRequest){
        User user = new User();
        updateUserFromRequest(user,userRequest);
        userRepo.save(user);
    }



    public UserResponse getById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id : "+id));
        return (mapToUserResponse(user));
    }

    public UserResponse updateUser(Long id, UserRequest updateduserRequest) {
        User existing= userRepo.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id : "+id));
        updateUserFromRequest(existing,updateduserRequest);
        User savedUser = userRepo.save(existing);

        return mapToUserResponse(savedUser);
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhone(userRequest.getPhone());
        user.setAuthUserId(user.getAuthUserId());

        if(userRequest.getAddress() != null){
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setCity(userRequest.getAddress().getCity());
            address.setState(userRequest.getAddress().getState());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setZipcode(userRequest.getAddress().getZipcode());

            user.setAddress(address);
        }

    }

    private UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());

        if(user.getAddress() != null){
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setState(user.getAddress().getState());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            response.setAddress(addressDTO);
        }

        return response;
    }

}
