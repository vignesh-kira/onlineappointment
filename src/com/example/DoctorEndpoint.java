package com.example;

import com.example.PMF;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
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
public class DoctorEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listDoctor")
	public CollectionResponse<Doctor> listDoctor(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit, User user) throws OAuthRequestException, IOException {
		if (user == null) throw new OAuthRequestException("User is Not Valid");
		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Doctor> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Doctor.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Doctor>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Doctor obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Doctor> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getDoctor")
	public Doctor getDoctor(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Doctor doctor = null;
		try {
			doctor = mgr.getObjectById(Doctor.class, id);
		} finally {
			mgr.close();
		}
		return doctor;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param doctor the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertDoctor")
	public Doctor insertDoctor(Doctor doctor) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (doctor.getId() != null) {
				if (containsDoctor(doctor)) {
					throw new EntityExistsException("Object already exists");
				}				
			}
			
			mgr.makePersistent(doctor);
		} finally {
			mgr.close();
		}
		return doctor;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param doctor the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateDoctor")
	public Doctor updateDoctor(Doctor doctor) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsDoctor(doctor)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(doctor);
		} finally {
			mgr.close();
		}
		return doctor;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeDoctor")
	public void removeDoctor(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Doctor doctor = mgr.getObjectById(Doctor.class, id);
			mgr.deletePersistent(doctor);
		} finally {
			mgr.close();
		}
	}

	private boolean containsDoctor(Doctor doctor) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Doctor.class, doctor.getId());
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
