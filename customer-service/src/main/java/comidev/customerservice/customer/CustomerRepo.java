package comidev.customerservice.customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import comidev.customerservice.region.Region;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
    boolean existsByDni(String dni);

    List<Customer> findByName(String name);

    List<Customer> findByRegion(Region region);

}
