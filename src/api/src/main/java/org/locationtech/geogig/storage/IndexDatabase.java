/* Copyright (c) 2017 Boundless and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/edl-v10.html
 *
 * Contributors:
 * Johnathan Garrett (Prominent Edge) - initial implementation
 */
package org.locationtech.geogig.storage;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;
import org.locationtech.geogig.model.ObjectId;
import org.locationtech.geogig.repository.IndexInfo;
import org.locationtech.geogig.repository.IndexInfo.IndexType;
import org.locationtech.geogig.repository.RepositoryConnectionException;

import com.google.common.base.Optional;

/**
 * The {@code IndexDatabase} keeps track of spatial and attribute indexes of user-specified data
 * sets.
 * 
 * @since 1.1
 */
public interface IndexDatabase extends ObjectStore {

    /**
     * Performs any setup required before first open, including setting default configuration.
     */
    public void configure() throws RepositoryConnectionException;

    /**
     * @return {@code true} if the {@code IndexDatabase} is read-only
     */
    public boolean isReadOnly();

    /**
     * Verify the configuration before opening the database.
     * 
     * @return {@code true} if the config was set, {@code false} otherwise
     * @throws RepositoryConnectionException if the config is incompatible
     */
    public boolean checkConfig() throws RepositoryConnectionException;

    /**
     * Create an index on the given feature type tree using the provided attribute name and indexing
     * strategy.
     * 
     * @param treeName the feature type name to index
     * @param attributeName the attribute to index
     * @param strategy the indexing strategy
     * @param metadata extra properties to be used by the index
     * @return the new index
     */
    public IndexInfo createIndexInfo(String treeName, String attributeName, IndexType strategy,
            @Nullable Map<String, Object> metadata);

    /**
     * Updates the metadata of an {@link IndexInfo} in the index database.
     * 
     * @param treeName the feature type name of the index info
     * @param attributeName the attribute of the index info
     * @param strategy the indexing strategy
     * @param metadata extra properties of the index info
     * @return the updated {@link IndexInfo}
     */
    public IndexInfo updateIndexInfo(String treeName, String attributeName, IndexType strategy,
            Map<String, Object> metadata);

    /**
     * Gets the index for the given tree and attribute if it exists.
     * 
     * @param treeName the name of the tree
     * @param attributeName the indexed attribute
     * @return an {@link Optional} with the index, or {@link Optional#absent()} if there wasn't an
     *         index
     */
    public Optional<IndexInfo> getIndexInfo(String treeName, String attributeName);

    /**
     * Gets all of the indexes for a given tree.
     * 
     * @param treeName the name of the tree
     * @return a list with all of the {@link IndexInfo} associated with the given tree
     */
    public List<IndexInfo> getIndexInfos(String treeName);
    
    /**
     * Gets all of the indexes in the database.
     * 
     * @return a list with all of the {@link IndexInfo} in the database
     */
    public List<IndexInfo> getIndexInfos();

    /**
     * Drops the given index from the database.
     * 
     * @param index the index to drop
     * @return {@code true} if the index was found and removed
     */
    public boolean dropIndex(IndexInfo index);

    /**
     * Removes all canonical/index tree associations for the given index.
     * 
     * @Param index the index to clear
     */
    public void clearIndex(IndexInfo index);

    /**
     * Associates an indexed tree with a tree from the {@link ObjectDatabase}.
     * 
     * @param index the index that the indexed tree belongs to
     * @param originalTree the {@link ObjectId} of the canonical tree
     * @param indexedTree the {@link ObjectId} of the indexed tree
     */
    public void addIndexedTree(IndexInfo index, ObjectId originalTree, ObjectId indexedTree);

    /**
     * Resolves a given tree id to the indexed version of the tree, if one exists.
     * 
     * @param index the index
     * @param treeId the {@link ObjectId} of the canonical tree
     */
    public Optional<ObjectId> resolveIndexedTree(IndexInfo index, ObjectId treeId);
}
