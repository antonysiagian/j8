package co.id.ant.j8.util;

import co.id.ant.j8.entity.Person;
import co.id.ant.j8.entity.PersonFullNameAndEmail;
import com.google.gson.Gson;


public class PersonUtil {

  private static final Gson gson = new Gson();

  public static void printFirstName(Person person){
    System.out.println(person.getFirstName());
  }

  public static void printPersonInJsonFormat(Person person){
    System.out.println("ThreadID: "+ Thread.currentThread().getId() +", Person: "+ gson.toJson(person));
  }

  public static void printPersonFullnameAndEmailInJsonFormat(PersonFullNameAndEmail person){
    System.out.println(gson.toJson(person));
  }

  public static String getJsonFormatFromPerson(Person aPerson){
    return gson.toJson(aPerson);
  }

}
