package com.openklaster.common.model;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
  private Date timestamp;
  @JsonAlias({ "installationid" })
  private String installationId;
  private String source;
  private String type;
  private String description;
}
