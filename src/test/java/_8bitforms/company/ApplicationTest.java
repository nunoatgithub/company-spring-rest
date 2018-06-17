package _8bitforms.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static Company company1;
    private static Company company2;

    @BeforeClass
    public static void init() {
        company1 = CompanyBuilder.aCompany()
                .withName("company1")
                .withAddress("address1")
                .withCity("city")
                .withCountry("be")
                .withEmail("some@one.com")
                .withPhoneNumber("123456789")
                .withOwners(Arrays.asList(OwnerBuilder.anOwner().withName("owner1").build()))
                .build();

        company2 = CompanyBuilder.aCompany()
                .withName("company2")
                .withAddress("address2")
                .withCity("city")
                .withCountry("be")
                .withEmail("some@one.com")
                .withPhoneNumber("123456789")
                .withOwners(Arrays.asList(OwnerBuilder.anOwner().withName("owner1").build()))
                .build();
    }

    @Test
    public void happyPath() throws Exception {

        this.mockMvc.perform(get("/companies")).andExpect(status().isOk())
                .andExpect(content().json("[]"));
        this.mockMvc.perform(get("/company/1")).andExpect(status().isNotFound())
                .andExpect(content().string(""));

        this.mockMvc
                .perform(post("/company")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(objectMapper.writeValueAsString(company1)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{'name':'company1','address':'address1','city':'city','country':'be','email':'some@one.com','phoneNumber':'123456789','owners':[{'name':'owner1'}],'id':1}"));

        this.mockMvc.perform(get("/companies")).andExpect(status().isOk())
                .andExpect(content().json("[{'name':'company1','address':'address1','city':'city','country':'be','email':'some@one.com','phoneNumber':'123456789','owners':[{'name':'owner1'}],'id':1}]"));

        this.mockMvc.perform(get("/company/1")).andExpect(status().isOk())
                .andExpect(content().json("{'name':'company1','address':'address1','city':'city','country':'be','email':'some@one.com','phoneNumber':'123456789','owners':[{'name':'owner1'}],'id':1}"));

        this.mockMvc
                .perform(post("/company")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(company2)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{'name':'company2','address':'address2','city':'city','country':'be','email':'some@one.com','phoneNumber':'123456789','owners':[{'name':'owner1'}],'id':2}"));

        this.mockMvc.perform(get("/companies")).andExpect(status().isOk())
                .andExpect(content().json("[{'name':'company1','address':'address1','city':'city','country':'be','email':'some@one.com','phoneNumber':'123456789','owners':[{'name':'owner1'}],'id':1}, " +
                                                     "{'name':'company2','address':'address2','city':'city','country':'be','email':'some@one.com','phoneNumber':'123456789','owners':[{'name':'owner1'}],'id':2}]"));

        company2.setName("updated company2");
        this.mockMvc
                .perform(put("/company/2")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(company2)))
                .andExpect(status().isOk())
                .andExpect(content().json("{'name':'updated company2','address':'address2','city':'city','country':'be','email':'some@one.com','phoneNumber':'123456789','owners':[{'name':'owner1'}],'id':2}"));

        this.mockMvc.perform(get("/companies")).andExpect(status().isOk())
                .andExpect(content().json("[{'name':'company1','address':'address1','city':'city','country':'be','email':'some@one.com','phoneNumber':'123456789','owners':[{'name':'owner1'}],'id':1}, " +
                                                     "{'name':'updated company2','address':'address2','city':'city','country':'be','email':'some@one.com','phoneNumber':'123456789','owners':[{'name':'owner1'}],'id':2}]"));

        this.mockMvc
                .perform(put("/company/2/owners")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(Arrays.asList(OwnerBuilder.anOwner().withName("owner2").build()))))
                .andExpect(status().isOk())
                .andExpect(content().json("{'name':'updated company2','address':'address2','city':'city','country':'be','email':'some@one.com','phoneNumber':'123456789','owners':[{'name':'owner1'}, {'name':'owner2'}],'id':2}"));


    }

    @Test
    public void createWithEmptyMandatoryFieldsFail() throws Exception {

        Company incomplete = CompanyBuilder.aCompany()
                .withName("incomplete")
                .build();
        this.mockMvc
                .perform(post("/company")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(incomplete)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void companiesCarryingIdFail() throws Exception {

        company1.setId(1L);
        this.mockMvc
                .perform(post("/company")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(company1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateNonExistent() throws Exception {

        Company company3 = CompanyBuilder.aCompany()
                .withName("company3")
                .withAddress("address3")
                .withCity("city")
                .withCountry("be")
                .withEmail("some@one.com")
                .withPhoneNumber("123456789")
                .withOwners(Arrays.asList(OwnerBuilder.anOwner().withName("owner1").build()))
                .build();

        this.mockMvc
                .perform(put("/company/3")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(company3)))
                .andExpect(status().isNotFound());
    }
}
