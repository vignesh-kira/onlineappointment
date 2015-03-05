/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2015-01-14 17:53:03 UTC)
 * on 2015-02-26 at 03:33:04 UTC 
 * Modify at your own risk.
 */

package com.example.clinic.model;

/**
 * Model definition for Appointment.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the clinic. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Appointment extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long doctorID;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long patientID;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String time;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getDoctorID() {
    return doctorID;
  }

  /**
   * @param doctorID doctorID or {@code null} for none
   */
  public Appointment setDoctorID(java.lang.Long doctorID) {
    this.doctorID = doctorID;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public Appointment setId(java.lang.Long id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getPatientID() {
    return patientID;
  }

  /**
   * @param patientID patientID or {@code null} for none
   */
  public Appointment setPatientID(java.lang.Long patientID) {
    this.patientID = patientID;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getTime() {
    return time;
  }

  /**
   * @param time time or {@code null} for none
   */
  public Appointment setTime(java.lang.String time) {
    this.time = time;
    return this;
  }

  @Override
  public Appointment set(String fieldName, Object value) {
    return (Appointment) super.set(fieldName, value);
  }

  @Override
  public Appointment clone() {
    return (Appointment) super.clone();
  }

}
