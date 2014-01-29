/**********************************************************************
Copyright (c) 2011 Andy Jefferson and others. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors:
   ...
**********************************************************************/
package org.datanucleus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.datanucleus.state.FetchPlanState;
import org.datanucleus.state.ObjectProvider;
import org.datanucleus.store.Extent;
import org.datanucleus.store.query.Query;

/**
 * ExecutionContext to attempt to handle multi-threaded PM/EM cases.
 * Locks various methods in an attempt to prevent conflicting thread updates.
 * Note we could have just put this code in ExecutionContextImpl but better to split it out since
 * the majority use-case is to have a non-thread-safe PM/EM.
 * TODO Evaluate all of the places we currently lock (when multithreaded) to find corner cases not caught.
 */
public class ExecutionContextThreadedImpl extends ExecutionContextImpl
{
    /**
     * @param ctx NucleusContext
     * @param owner Owner object (PM, EM)
     * @param options Any options affecting startup
     */
    public ExecutionContextThreadedImpl(PersistenceNucleusContext ctx, Object owner, Map<String, Object> options)
    {
        super(ctx, owner, options);
    }

    /**
     * Accessor for whether the object manager is multithreaded.
     * @return Whether to run multithreaded.
     */
    public boolean getMultithreaded()
    {
        return true;
    }

    public void processNontransactionalUpdate()
    {
        try
        {
            lock.lock();

            super.processNontransactionalUpdate();
        }
        finally
        {
            lock.unlock();
        }
    }

    public void enlistInTransaction(ObjectProvider sm)
    {
        try
        {
            lock.lock();

            super.enlistInTransaction(sm);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void evictFromTransaction(ObjectProvider sm)
    {
        try
        {
            lock.lock();

            super.evictFromTransaction(sm);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void addObjectProvider(ObjectProvider op)
    {
        try
        {
            lock.lock();

            super.addObjectProvider(op);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void removeObjectProvider(ObjectProvider op)
    {
        try
        {
            lock.lock();

            super.removeObjectProvider(op);
        }
        finally
        {
            lock.unlock();
        }
    }

    public ObjectProvider findObjectProvider(Object pc)
    {
        try
        {
            lock.lock();

            return super.findObjectProvider(pc);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void hereIsObjectProvider(ObjectProvider sm, Object pc)
    {
        try
        {
            lock.lock();

            super.hereIsObjectProvider(sm, pc);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void close()
    {
        try
        {
            lock.lock();

            super.close();
        }
        finally
        {
            lock.unlock();
        }
    }

    public void evictObject(Object obj)
    {
        try
        {
            lock.lock();

            super.evictObject(obj);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void refreshObject(Object obj)
    {
        try
        {
            lock.lock();

            super.refreshObject(obj);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void retrieveObject(Object obj, boolean fgOnly)
    {
        try
        {
            lock.lock();

            super.retrieveObject(obj, fgOnly);
        }
        finally
        {
            lock.unlock();
        }
    }

    public Object persistObject(Object obj, boolean merging)
    {
        try
        {
            lock.lock();

            return super.persistObject(obj, merging);
        }
        finally
        {
            lock.unlock();
        }
    }

    public Object[] persistObjects(Object[] objs)
    {
        try
        {
            lock.lock();

            return super.persistObjects(objs);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void deleteObject(Object obj)
    {
        try
        {
            lock.lock();

            super.deleteObject(obj);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void deleteObjects(Object[] objs)
    {
        try
        {
            lock.lock();

            super.deleteObjects(objs);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void makeObjectTransient(Object obj, FetchPlanState state)
    {
        try
        {
            lock.lock();

            super.makeObjectTransient(obj, state);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void makeObjectTransactional(Object obj)
    {
        try
        {
            lock.lock();

            super.makeObjectTransactional(obj);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void attachObject(ObjectProvider ownerOP, Object pc, boolean sco)
    {
        try
        {
            lock.lock();

            super.attachObject(ownerOP, pc, sco);
        }
        finally
        {
            lock.unlock();
        }
    }

    public Object attachObjectCopy(ObjectProvider ownerOP, Object pc, boolean sco)
    {
        try
        {
            lock.lock();

            return super.attachObjectCopy(ownerOP, pc, sco);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void detachObject(Object obj, FetchPlanState state)
    {
        try
        {
            lock.lock();

            super.detachObject(obj, state);
        }
        finally
        {
            lock.unlock();
        }
    }

    public Object detachObjectCopy(Object pc, FetchPlanState state)
    {
        try
        {
            lock.lock();

            return super.detachObjectCopy(pc, state);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void clearDirty(ObjectProvider op)
    {
        try
        {
            lock.lock();

            super.clearDirty(op);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void clearDirty()
    {
        try
        {
            lock.lock();

            super.clearDirty();
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * Method to evict all current objects from L1 cache.
     */
    public void evictAllObjects()
    {
        assertIsOpen();

        try
        {
            lock.lock();

            synchronized (cache)
            {
                // All persistent non-transactional instances should be evicted here, but not yet supported
                ArrayList<ObjectProvider> opsToEvict = new ArrayList();
                opsToEvict.addAll(cache.values());

                // Evict ObjectProviders and remove objects from cache
                // Performed in separate loop to avoid ConcurrentModificationException
                Iterator<ObjectProvider> opIter = opsToEvict.iterator();
                while (opIter.hasNext())
                {
                    ObjectProvider op = opIter.next();
                    Object pc = op.getObject();
                    op.evict();

                    // Evict from L1
                    removeObjectFromLevel1Cache(getApiAdapter().getIdForObject(pc));
                }
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    public void markDirty(ObjectProvider op, boolean directUpdate)
    {
        try
        {
            lock.lock();

            super.markDirty(op, directUpdate);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void flush()
    {
        try
        {
            lock.lock();

            super.flush();
        }
        finally
        {
            lock.unlock();
        }
    }

    public void flushInternal(boolean flushToDatastore)
    {
        try
        {
            lock.lock();

            super.flushInternal(flushToDatastore);
        }
        finally
        {
            lock.unlock();
        }
    }

    public void replaceObjectId(Object pc, Object oldID, Object newID)
    {
        try
        {
            lock.lock();

            super.replaceObjectId(pc, oldID, newID);
        }
        finally
        {
            lock.unlock();
        }
    }

    public Extent getExtent(Class pcClass, boolean subclasses)
    {
        try
        {
            lock.lock();

            return super.getExtent(pcClass, subclasses);
        }
        finally
        {
            lock.unlock();
        }
    }

    public Query newQuery()
    {
        try
        {
            lock.lock();

            return super.newQuery();
        }
        finally
        {
            lock.unlock();
        }
    }
}