package com.example;

import com.example.PMF;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

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
public class QueueEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listQueue")
	public CollectionResponse<Queue> listQueue(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<Queue> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(Queue.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<Queue>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Queue obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Queue> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getQueue")
	public Queue getQueue(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		Queue queue = null;
		try {
			queue = mgr.getObjectById(Queue.class, id);
		} finally {
			mgr.close();
		}
		return queue;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param queue the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertQueue")
	public Queue insertQueue(Queue queue) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (queue.getId() != null) {
				if (containsQueue(queue)) {
					throw new EntityExistsException("Object already exists");
				}
			}
			
			mgr.makePersistent(queue);
		} finally {
			mgr.close();
		}
		return queue;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param queue the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateQueue")
	public Queue updateQueue(Queue queue) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsQueue(queue)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(queue);
		} finally {
			mgr.close();
		}
		return queue;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeQueue")
	public void removeQueue(@Named("id") Long id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			Queue queue = mgr.getObjectById(Queue.class, id);
			mgr.deletePersistent(queue);
		} finally {
			mgr.close();
		}
	}

	private boolean containsQueue(Queue queue) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(Queue.class, queue.getId());
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
