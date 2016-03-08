/**
 * Copyright (C) 2010-2016 Structr GmbH
 *
 * This file is part of Structr <http://structr.org>.
 *
 * Structr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Structr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Structr.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.structr.schema.export;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.TreeMap;
import org.structr.common.error.FrameworkException;
import org.structr.core.app.App;
import org.structr.core.app.StructrApp;
import org.structr.core.entity.Relation.Cardinality;
import org.structr.core.entity.SchemaNode;
import org.structr.core.entity.SchemaRelationshipNode;
import org.structr.schema.json.JsonObjectType;
import org.structr.schema.json.JsonReferenceType;
import org.structr.schema.json.JsonSchema;

/**
 *
 *
 */
public class StructrNodeTypeDefinition extends StructrTypeDefinition<SchemaNode> implements JsonObjectType {

	StructrNodeTypeDefinition(final StructrSchemaDefinition root, final String name) {
		super(root, name);
	}

	@Override
	public JsonReferenceType relate(final JsonObjectType type) throws URISyntaxException {
		return relate(type, SchemaRelationshipNode.getDefaultRelationshipType(getName(), type.getName()));
	}

	@Override
	public JsonReferenceType relate(URI externalTypeReference) throws URISyntaxException {

		final Class type = StructrApp.resolveSchemaId(externalTypeReference);
		if (type != null) {

			return relate(externalTypeReference, SchemaRelationshipNode.getDefaultRelationshipType(getName(), type.getSimpleName()));
		}

		throw new IllegalStateException("External reference " + externalTypeReference + " not found.");
	}

	@Override
	public JsonReferenceType relate(final JsonObjectType type, final String relationship) throws URISyntaxException {
		return relate(type, relationship, Cardinality.ManyToMany);
	}

	@Override
	public JsonReferenceType relate(URI externalTypeReference, String relationship) throws URISyntaxException {
		return relate(externalTypeReference, relationship, Cardinality.ManyToMany);
	}

	@Override
	public JsonReferenceType relate(final JsonObjectType type, final String relationship, final Cardinality cardinality) throws URISyntaxException {

		final String sourcePropertyName = getPropertyName(type.getName(), true,  relationship, cardinality);
		final String targetPropertyName = getPropertyName(type.getName(), false, relationship, cardinality);

		return relate(type, relationship, cardinality, sourcePropertyName, targetPropertyName);
	}

	@Override
	public JsonReferenceType relate(URI externalTypeReference, String relationship, Cardinality cardinality) throws URISyntaxException {

		final Class type = StructrApp.resolveSchemaId(externalTypeReference);
		if (type != null) {

			final String sourcePropertyName = getPropertyName(type.getSimpleName(), true,  relationship, cardinality);
			final String targetPropertyName = getPropertyName(type.getSimpleName(), false, relationship, cardinality);

			return relate(externalTypeReference, relationship, cardinality, sourcePropertyName, targetPropertyName);
		}

		throw new IllegalStateException("External reference " + externalTypeReference + " not found.");
	}

	@Override
	public JsonReferenceType relate(final JsonObjectType type, final String relationship, final Cardinality cardinality, final String sourceAttributeName, final String targetAttributeName) throws URISyntaxException {

		final String relationshipTypeName           = getName() + relationship + type.getName();
		final StructrRelationshipTypeDefinition def = new StructrRelationshipTypeDefinition(root, relationshipTypeName);

		// initialize
		def.setSourcePropertyName(sourceAttributeName);
		def.setTargetPropertyName(targetAttributeName);
		def.setRelationship(relationship);
		def.setCardinality(cardinality);
		def.setSourceType(getId());
		def.setTargetType(type.getId());

		root.addType(def);

		return def;
	}

	@Override
	public JsonReferenceType relate(URI externalTypeReference, String relationship, Cardinality cardinality, String sourceAttributeName, String targetAttributeName) throws URISyntaxException {

		final Class type = StructrApp.resolveSchemaId(externalTypeReference);
		if (type != null) {

			final String relationshipTypeName           = getName() + relationship + type.getSimpleName();
			final StructrRelationshipTypeDefinition def = new StructrRelationshipTypeDefinition(root, relationshipTypeName);

			// initialize
			def.setSourcePropertyName(sourceAttributeName);
			def.setTargetPropertyName(targetAttributeName);
			def.setRelationship(relationship);
			def.setCardinality(cardinality);
			def.setSourceType(getId());
			def.setTargetType(externalTypeReference);

			root.addType(def);

			return def;

		}

		throw new IllegalStateException("External reference " + externalTypeReference + " not found.");
	}

	// ----- package methods -----
	@Override
	Map<String, Object> serialize() {

		final Map<String, Object> map = super.serialize();

		Map<String, Object> serializedProperties = (Map)map.get(JsonSchema.KEY_PROPERTIES);
		if (serializedProperties == null) {

			serializedProperties = new TreeMap<>();
			map.put(JsonSchema.KEY_PROPERTIES, serializedProperties);
		}

		// serialize remote properties
		for (final StructrRelationshipTypeDefinition rel : root.getRelationships()) {

			if (getId().equals(rel.getSourceType())) {

				// outgoing
				final Map<String, Object> property = rel.serializeRelationshipProperty(true);
				if (property != null) {

					serializedProperties.put(rel.getTargetPropertyName(), property);
				}
			}

			if (getId().equals(rel.getTargetType())) {

				// incoming
				final Map<String, Object> property = rel.serializeRelationshipProperty(false);
				if (property != null) {

					serializedProperties.put(rel.getSourcePropertyName(), property);
				}
			}
		}

		// remove empty objects from json
		if (serializedProperties.isEmpty()) {
			map.remove(JsonSchema.KEY_PROPERTIES);
		}

		return map;
	}

	@Override
	void deserialize(final Map<String, Object> source) {
		super.deserialize(source);
	}

	@Override
	SchemaNode createSchemaNode(App app) throws FrameworkException {
		return app.create(SchemaNode.class, getName());
	}

	// ----- private methods -----
	private String getPropertyName(final String targetTypeName, final boolean outgoing, final String relationshipTypeName, final Cardinality cardinality) {

		final String sourceTypeName   = getName();
		final String relatedClassName = outgoing ? targetTypeName : sourceTypeName;

		String _sourceMultiplicity = null;
		String _targetMultiplicity = null;

		switch (cardinality) {

			case OneToOne:
				_sourceMultiplicity = "1";
				_targetMultiplicity = "1";
				break;

			case OneToMany:
				_sourceMultiplicity = "1";
				_targetMultiplicity = "*";
				break;

			case ManyToOne:
				_sourceMultiplicity = "*";
				_targetMultiplicity = "1";
				break;

			case ManyToMany:
				_sourceMultiplicity = "*";
				_targetMultiplicity = "*";
				break;
		}

		if (!outgoing) {

			// swap multiplicities
			final String tmp    = _sourceMultiplicity;
			_sourceMultiplicity = _targetMultiplicity;
			_targetMultiplicity = tmp;
		}


		return SchemaRelationshipNode.getPropertyName(relatedClassName, root.getExistingPropertyNames(), outgoing, relationshipTypeName, sourceTypeName, targetTypeName, null, _targetMultiplicity, null, _sourceMultiplicity);
	}
}