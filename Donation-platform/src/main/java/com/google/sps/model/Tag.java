import java.util.List;
import java.util.Objects;

/** Represents a Tag : id, name, trending score */
public class Tag {

  private Integer id;
  private String name;
  private Integer trendingScore;
  // [START fs_class_definition]
  public Tag() {
    // Must have a public no-argument constructor
  }

  // Initialize all fields of a Tag
  public Tag(Integer id, String name, Integer trendingScore) {
    this.id = id;
    this.name = name;
    this.trendingScore = trendingScore;
    
  }
  // [END fs_class_definition]

  public Tag(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getTrendingScore() {
    return trendingScore;
  }

  public void setTrendingScore(Integer trendingScore) {
    this.trendingScore = trendingScore;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, trendingScore);
  }
}