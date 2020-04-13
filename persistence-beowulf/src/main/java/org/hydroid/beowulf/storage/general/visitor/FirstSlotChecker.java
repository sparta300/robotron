package org.hydroid.beowulf.storage.general.visitor;

import org.hydroid.beowulf.storage.Slot;
import org.hydroid.file.PhysicalResourceException;



public interface FirstSlotChecker
{
    /**
     * performs any necessary checks on the first slot
     * @param firstSlot the first slot of the serialised object
     * @throws PhysicalResourceException when the check fails
     */
    public void checkFirstSlot(Slot firstSlot) throws PhysicalResourceException;
}
