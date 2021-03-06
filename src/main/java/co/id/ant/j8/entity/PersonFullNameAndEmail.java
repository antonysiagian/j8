package co.id.ant.j8.entity;

public class PersonFullNameAndEmail {

  private String firstName;
  private String lastName;
  private String email;

  public PersonFullNameAndEmail(Person person){
    this.firstName = person.getFirstName();
    this.lastName = person.getLastName();
    this.email = person.getEmail();
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFullNameAndEmail(){
    return this.firstName.concat(" ").concat(this.lastName).concat(" -  ").concat(this.email);
  }
}
