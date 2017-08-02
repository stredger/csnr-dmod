package ca.bc.gov.nrs.dm.microservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;


/**
 * Primary object type
 **/

import io.swagger.annotations.*;
import java.util.Objects;
@ApiModel(description = "Primary object type")

public class Document   {
  
  private Integer id = null;
  private String filename = null;

  /**
   * A system-generated unique identifier for a Document
   **/
  public Document id(Integer id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(example = "null", required = true, value = "A system-generated unique identifier for a Document")
  @JsonProperty("id")
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Document Filename
   **/
  public Document filename(String filename) {
    this.filename = filename;
    return this;
  }

  
  @ApiModelProperty(example = "null", value = "Document Filename")
  @JsonProperty("filename")
  public String getFilename() {
    return filename;
  }
  public void setFilename(String filename) {
    this.filename = filename;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Document document = (Document) o;
    return Objects.equals(id, document.id) &&
        Objects.equals(filename, document.filename);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, filename);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Document {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    filename: ").append(toIndentedString(filename)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

