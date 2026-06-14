package com.lalit.user.service;

import com.lalit.user.dto.AddressDTO;
import com.lalit.user.dto.UserRequest;
import com.lalit.user.dto.UserResponse;
import com.lalit.user.entity.Address;
import com.lalit.user.entity.User;
import com.lalit.user.repository.UserRepo;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
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
//        user.setId(count++);
        User user = new User();
        updateUserFromRequest(user,userRequest);
        userRepo.save(user);
    }



    public Optional<UserResponse> getById(Long id) {
//        return userList.stream()
//                .filter(user -> user.getId().equals(id))
//                .findFirst();

        return userRepo.findById(id)
                .map(this::mapToUserResponse);
    }

    public boolean updateUser(Long id, UserRequest updateduserRequest) {
//        return userList.stream()
//                .filter(user -> user.getId().equals(id))
//                .findFirst()
//                .map(existing -> {
//                    existing.setFirstName(updateduser.getFirstName());
//                    existing.setLastName(updateduser.getLastName());
//                    return true;
//                })
//                .orElse(false);

        return userRepo.findById(id)
                .map(existing -> {
                    updateUserFromRequest(existing,updateduserRequest);
                    userRepo.save(existing);
                    return true;
                }).orElse(false);
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
//        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
//        response.setRole(user.getRole());

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
