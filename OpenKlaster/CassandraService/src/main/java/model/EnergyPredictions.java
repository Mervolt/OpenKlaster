package model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Date;

@Table(keyspace = "openklaster", name = "energypredictions")
public class EnergyPredictions {
  @PartitionKey
  private Date timestamp;
  private int installationId;
  private String source;
  private String type;
  private String description;

  public EnergyPredictions(Date timestamp, int installationId, String source, String type, String description) {
    this.timestamp = timestamp;
    this.installationId = installationId;
    this.source = source;
    this.type = type;
    this.description = description;
  }

  @Override
  public String toString() {
    return "EnergyPredictions{" +
            "timestamp=" + timestamp +
            ", installationId=" + installationId +
            ", source='" + source + '\'' +
            ", type='" + type + '\'' +
            ", description='" + description + '\'' +
            '}';
  }
}
