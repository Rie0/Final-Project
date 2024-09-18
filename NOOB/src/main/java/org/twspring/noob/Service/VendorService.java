package org.twspring.noob.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.twspring.noob.Api.ApiException;
import org.twspring.noob.DTO.VendotDTO;
import org.twspring.noob.Model.User;
import org.twspring.noob.Model.Vendor;
import org.twspring.noob.Repository.AuthRepository;
import org.twspring.noob.Repository.VendorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorService {
    private final VendorRepository vendorRepository;
    private final AuthRepository authRepository;

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public void registerVendor(VendotDTO vendotDTO) {
        String hash = new BCryptPasswordEncoder().encode(vendotDTO.getPassword());
        User user = new User();
        user.setUsername(vendotDTO.getUsername());
        user.setPassword(hash);
        user.setEmail(vendotDTO.getEmail());
        user.setPhoneNumber(vendotDTO.getPhoneNumber());
        user.setRole("VENDOR");
        authRepository.save(user);

        Vendor vendor = new Vendor();
        vendor.setVenueName(vendotDTO.getVenueName());
        vendor.setUser(user);
        vendorRepository.save(vendor);
    }

    public void updateVendor(Integer vendorId, VendotDTO vendotDTO) {
        String hash = new BCryptPasswordEncoder().encode(vendotDTO.getPassword());

        Vendor vendor = vendorRepository.findVendorById(vendorId);
        User user = vendor.getUser();

        user.setEmail(vendotDTO.getEmail());
        user.setPassword(hash);
        user.setPhoneNumber(vendotDTO.getPhoneNumber());
        authRepository.save(user);

        vendor.setVenueName(vendotDTO.getVenueName());
        vendorRepository.save(vendor);
    }

    public void deleteVendor(Integer vendorId) {
        authRepository.deleteById(vendorId);
    }

    //// =============
    public Vendor getVendorById(Integer vendorId) {
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("vendor not found");

        }
        return vendor;

    }

}
