package com.openklaster.common.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(keyspace = "openklaster", name = "weatherconditions")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WeatherConditions {
  @PartitionKey
  private Date timestamp;
  private String installationId;
  private String source;
  private String type;
  private String description;
}