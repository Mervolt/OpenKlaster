package openklaster.cassandra.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;

@Table(keyspace = "openklaster", name = "weatherconditions")
public class WeatherConditions {
  @PartitionKey
  private Date timestamp;
  private int installationId;
  private String source;
  private String type;
  private String description;

  public WeatherConditions(Date timestamp, int installationId, String source, String type, String description) {
    this.timestamp = timestamp;
    this.installationId = installationId;
    this.source = source;
    this.type = type;
    this.description = description;
  }

  public WeatherConditions() {
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public int getInstallationId() {
    return installationId;
  }

  public void setInstallationId(int installationId) {
    this.installationId = installationId;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
