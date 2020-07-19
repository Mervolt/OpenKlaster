package com.openklaster.common.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(keyspace = "openklaster", name = "energypredictions")
@NoArgsConstructor
@AllArgsConstructor
public class EnergyPredictions {
  @PartitionKey
  private Date timestamp;
  private int installationId;
  private String source;
  private String type;
  private String description;

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
