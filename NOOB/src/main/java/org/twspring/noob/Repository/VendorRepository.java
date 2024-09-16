package org.twspring.noob.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.twspring.noob.Model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    Vendor findVendorById(Integer id);

}
