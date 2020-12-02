package com.pradhan.ebanking.eBanking_Backend.Repository;


import com.pradhan.ebanking.eBanking_Backend.beans.Customer;
import com.pradhan.ebanking.eBanking_Backend.repository.CustomerRepository;
import com.pradhan.ebanking.eBanking_Backend.service.CustomerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.BDDMockito.*;


@RunWith(MockitoJUnitRunner.class)
public class RepositoryTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Before
    public void init() {

    }


    @Test
    public void findByUserName() {

        // So Mock Customer class? or Repository Class (I think mock repo class)
        Customer customer =  new Customer();
        customer.setFirstName("Suma");
        customer.setLastName("Prad");
        customer.setUserName("sims");
        customer.setPassword("xxxx");
        customer.setEmail("sumprqqad@email.com");
        customer.setAddress("asdasd");
        customer.setPhoneNumber("78411");
        // creating a prototypes in a seperate class reduces code clusters

        given(customerRepository.findByUserName("sims")).willReturn(customer);

        Customer unExpected = customerRepository.findByUserName("thanos");
        Customer expected = customerRepository.findByUserName("sims");

        Assert.assertEquals(customer, expected);
        Assert.assertNotEquals(customer, unExpected);

    }

}
