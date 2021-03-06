/*******************************************************************************
 * Copyright (c) 2014 MEDEVIT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     T. Huster - initial API and implementation
 *******************************************************************************/
package at.medevit.elexis.inbox.model.impl;

import java.util.HashSet;
import java.util.List;

import at.medevit.elexis.inbox.model.IInboxElementService;
import at.medevit.elexis.inbox.model.IInboxUpdateListener;
import at.medevit.elexis.inbox.model.InboxElement;
import ch.elexis.data.Kontakt;
import ch.elexis.data.Mandant;
import ch.elexis.data.Patient;
import ch.elexis.data.PersistentObject;
import ch.elexis.data.Query;

public class InboxElementService implements IInboxElementService {
	
	HashSet<IInboxUpdateListener> listeners = new HashSet<IInboxUpdateListener>();
	
	@Override
	public void createInboxElement(Patient patient, Kontakt mandant, PersistentObject object){
		InboxElement element = new InboxElement(patient, mandant, object);
		fireUpdate(element);
	}
	
	@Override
	public void removeUpdateListener(IInboxUpdateListener listener){
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	@Override
	public void addUpdateListener(IInboxUpdateListener listener){
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	@Override
	public List<InboxElement> getInboxElements(Mandant mandant, Patient patient, State state){
		Query<InboxElement> qie = new Query<InboxElement>(InboxElement.class);
		if (mandant != null) {
			qie.add(InboxElement.FLD_MANDANT, "=", mandant.getId());
		}
		if (patient != null) {
			qie.add(InboxElement.FLD_PATIENT, "=", patient.getId());
		}
		if (state != null) {
			qie.add(InboxElement.FLD_STATE, "=", Integer.toString(state.ordinal()));
		}
		return qie.execute();
	}
	
	@Override
	public void changeInboxElementState(InboxElement element, State state){
		element.set(InboxElement.FLD_STATE, Integer.toString(state.ordinal()));
		fireUpdate(element);
	}
	
	private void fireUpdate(InboxElement element){
		synchronized (listeners) {
			for (IInboxUpdateListener listener : listeners) {
				listener.update(element);
			}
		}
	}
	
	public void activate(){
		System.out.println("active providers");
		ElementsProviderExtension.activateAll();
	}
	
	public void deactivate(){
		System.out.println("deactive providers");
	}
	
}
