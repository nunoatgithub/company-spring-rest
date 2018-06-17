package _8bitforms.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@Transactional
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Data integrity violation")  // 400
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void badRequest() {
        // Nothing to do
    }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal Server Error")  // 500
    @ExceptionHandler(Exception.class)
    public void systemError() {
        // Nothing to do
    }

    @RequestMapping(path="/company", method=POST)
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {

        if (company.getId() != null) {
            return new ResponseEntity<>((Company)null, HttpStatus.BAD_REQUEST);
        }
        Optional.ofNullable(company.getOwners())
            .ifPresent(owners -> owners.stream().forEach(ownerRepository::save));
        return new ResponseEntity<>(companyRepository.save(company), HttpStatus.CREATED);
    }

    @RequestMapping(path="/companies", method= GET)
    public List<Company> getAllCompanies() {

        List<Company> result = new ArrayList<>();
        companyRepository.findAll().forEach(result::add);
        return result;
    }

    @RequestMapping(path="/company/{id}", method=GET)
    public ResponseEntity<Company> getCompany(@PathVariable("id") Long id) {

        Company existing = companyRepository.findOne(id);
        if (existing == null) {
            return new ResponseEntity<>((Company)null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(existing, HttpStatus.OK);
    }

    @RequestMapping(path="/company/{id}", method=PUT)
    public ResponseEntity<Company> updateCompany(@PathVariable("id") Long id, @RequestBody Company company) {

        Company existing = companyRepository.findOne(id);
        if (existing == null) {
            return new ResponseEntity<>((Company)null, HttpStatus.NOT_FOUND);
        }
        company.setId(id);
        company.getOwners().stream().forEach(ownerRepository::save);
        return new ResponseEntity<>(companyRepository.save(company), HttpStatus.OK);
    }

    @RequestMapping(path="/company/{id}/owners", method=PUT)
    public ResponseEntity<Company> addOwners(@PathVariable("id") Long id, @RequestBody List<Owner> owners) {

        Company existing = companyRepository.findOne(id);
        if (existing == null) {
            return new ResponseEntity<>((Company)null, HttpStatus.NOT_FOUND);
        }

        owners.stream().forEach(ownerRepository::save);

        List<Owner> companyOwners = existing.getOwners();
        if (companyOwners == null) {
            companyOwners = new ArrayList<>();
        }
        companyOwners.addAll(owners);
        existing.setOwners(companyOwners.stream().distinct().collect(Collectors.toList()));
        return new ResponseEntity<>(companyRepository.save(existing), HttpStatus.OK);
    }
}
