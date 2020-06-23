import java.util.List;
import java.util.Objects;

/** Represents a Users : id, userName, email, userInterests, charitiesDonatedTo. */
public class Users {

  private Integer id;
  private String userName;
  private String email;
  private List<Integer> userInterests;
  private List<Integer>  charitiesDonatedTo;


  // [START fs_class_definition]
  public Users() {
    // Must have a public no-argument constructor
  }

  // Initialize all fields of a Users
  public Users(Integer id, String userName, String email,
              List<Integer> userInterests, List<Integer>  charitiesDonatedTo) {
    this.id = id;
    this.userName = userName;
    this.email = email;
    this.userInterests = userInterests;
    this.charitiesDonatedTo = charitiesDonatedTo;
  }
  // [END fs_class_definition]

  public Users(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Integer> getUserInterests() {
    return userInterests;
  }

  public void setUserInterests(List<Integer> userInterests) {
    this.userInterests = userInterests;
  }

  public List<Integer> getCharitiesDonatedTo() {
    return charitiesDonatedTo;
  }

  public void setCharitiesDonatedTo(List<Integer> charitiesDonatedTo) {
    this.charitiesDonatedTo = charitiesDonatedTo;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userName, email, userInterests, charitiesDonatedTo);
  }
}