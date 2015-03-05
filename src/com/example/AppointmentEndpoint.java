package com.example;

import com.example.PMF;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import java.io.IOException;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "clinic", version = "v1", scopes = {Constants.EMAIL_SCOPE},
clientIds = {Constants.WEB_CLIENT_ID, 
	     Constants.ANDROID_CLIENT_ID, 
	     com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
	     audiences = {Constants.ANDROID_AUDIENCE}, namespace = @ApiNamespace(ownerDomain = "example.com", ownerName = "example.com", packagePath = ""))
public class AppointmentEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 * shows all appointments of each doctor
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "doctorsAppointmentsList", 
            path="doctors/{doctorID}/appointment")
	public List<Appointment> listAppointmentDoctor(@Named ("doctorID") Long doctorID, User user) throws OAuthRequestException, IOException {
		if (user == null) throw new OAuthRequestException("User is Not Valid");
		PersistenceManager mgr = getPersistenceManager();
		List<Appointment> execute = null;

		try {
			Query query = mgr.newQuery(Appointment.class, "doctorID == " + doctorID);
			execute = (List<Appointment>) query.execute();
			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Appointment appointment : execute);
		} finally {
			mgr.close();
		}

		return execute;
	}
	
	/** listing patients' appointments */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "patientsAppointmentsList", 
            path="patients/{patientID}/appointment")
	public List<Appointment> listAppointmentPatient(@Named ("patientID") Long patientID, User user) throws OAuthRequestException, IOException {
		if (user == null) throw new OAuthRequestException("User is Not Valid");
		PersistenceManager mgr = getPersistenceManager();
		List<Appointment> execute = null;

		try {
			Query query = mgr.newQuery(Appointment.class, "patientID == " + patientID);
			execute = (List<Appointment>) query.execute();
			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Appointment appointment : execute);
		} finally {
			mgr.close();
		}

		return execute;
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "doctorsAppointmentsGet", 
            path="doctors/{doctorID}/appointment/{id}")
	public Appointment getAppointment(@Named("doctorID") Long doctorID, @Named("id")  Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Appointment appointment = null;
		try {
			appointment = mgr.getObjectById(Appointment.class, id);
		} finally {
			mgr.close();
		}
		return appointment;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param appointment the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "doctorsAppointmentsInsert", 
            path="doctors/{doctorID}/appointment")
	public Appointment insertAppointment(@Named ("doctorID") Long doctorID, @Named ("time") Long time, @Named ("date") Long date, Appointment appointment, User user) throws OAuthRequestException, IOException {
		if (user == null) throw new OAuthRequestException("User is Not Valid");
		appointment.setDoctorID(doctorID);
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (appointment.getId() != null) {
				if (containsAppointment(appointment)) {
					throw new EntityExistsException("Object already exists");
				}				
			}
			
			mgr.makePersistent(appointment);
		} finally {
			mgr.close();
		}
		return appointment;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param appointment the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "doctorsAppointmentsUpdate", 
            path="doctors/{doctorID}/appointment/{id}")
	public Appointment updateAppointment(@Named ("doctorID") Long doctorID, @Named("id")  Long id, Appointment appointment, User user) throws OAuthRequestException, IOException  {
		if (user == null) throw new OAuthRequestException("User is Not Valid");
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (appointment.getId() != null) {
				if (!containsAppointment(appointment)) {
					throw new EntityNotFoundException("Object does not exist");
				}
			}
			
			mgr.makePersistent(appointment);
		} finally {
			mgr.close();
		}
		
		return appointment;
		
		
	}

	
	@ApiMethod(name = "patientMakesAppointment", 
            path="patients/{patientID}/appointment/{id}")
	public Appointment makeAppointmentPatients(@Named ("patientID") Long patientID, @Named("id")  Long id, Appointment appointment, User user) {
		PersistenceManager mgr2 = getPersistenceManager();
		Appointment appointment2 = null;
		try {
			appointment2 = mgr2.getObjectById(Appointment.class, id);
		} finally {
			mgr2.close();
		}
		
		String Ttime;
		String Tdate;
		Long  TdoctorID ;
		
		Ttime = appointment2.getTime();
		Tdate = appointment2.getDate();
		TdoctorID = appointment2.getDoctorID();
		
		appointment.setTime(Ttime);
		appointment.setDate(Tdate);
		appointment.setDoctorID(TdoctorID);
		appointment.setPatientID(patientID);


		
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (appointment.getId() != null) {
				if (!containsAppointment(appointment)) {
					throw new EntityNotFoundException("Object does not exist");
				}
			}
			
			mgr.makePersistent(appointment);
		} finally {
			mgr.close();
		}
		return appointment;
	}
		
	
	
	@ApiMethod(name = "patientCanceledAppointment", 
            path="patients/{id}/appointment")
	public Appointment canceledAppointmentPatients(@Named("id")  Long id, Appointment appointment, User user) {
		PersistenceManager mgr2 = getPersistenceManager();
		Appointment appointment2 = null;
		try {
			appointment2 = mgr2.getObjectById(Appointment.class, id);
		} finally {
			mgr2.close();
		}
		
		String Ttime;
		String Tdate;
		Long  TdoctorID ;
		
		Ttime = appointment2.getTime();
		Tdate = appointment2.getDate();
		TdoctorID = appointment2.getDoctorID();
		
		appointment.setTime(Ttime);
		appointment.setDate(Tdate);
		appointment.setDoctorID(TdoctorID);

		PersistenceManager mgr = getPersistenceManager();
		try {
			if (appointment.getId() != null) {
				if (!containsAppointment(appointment)) {
					throw new EntityNotFoundException("Object does not exist");
				}
			}
			
			mgr.makePersistent(appointment);
		} finally {
			mgr.close();
		}
		return appointment;
	}
	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "doctorsAppointmentsDelete", 
            path="doctors/{doctorID}/appointment/{id}")
	public void removeAppointment(@Named ("doctorID") Long doctorID, @Named ("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Appointment appointment = null;
		try {
			appointment = mgr.getObjectById(Appointment.class, id);
			mgr.deletePersistent(appointment);
		} finally {
			mgr.close();
		}
	}

	private boolean containsAppointment(Appointment appointment) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Appointment.class, appointment.getId());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
