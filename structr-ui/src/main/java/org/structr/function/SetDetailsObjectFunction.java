/**
 * Copyright (C) 2010-2016 Structr GmbH
 *
 * This file is part of Structr <http://structr.org>.
 *
 * Structr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Structr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Structr.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.structr.function;

import org.structr.core.GraphObject;
import org.structr.schema.action.ActionContext;
import org.structr.web.common.RenderContext;

/**
 *
 */
public class SetDetailsObjectFunction extends UiFunction {

	public static final String ERROR_MESSAGE_SET_DETAILS_OBJECT    = "Usage: ${set_details_object(obj)}. Example: ${set_details_object(this)}";
	public static final String ERROR_MESSAGE_SET_DETAILS_OBJECT_JS = "Usage: ${{Structr.setDetailsObject(obj)}}. Example: ${{Structr.setDetailsObject(Structr.this)}}";

	@Override
	public String getName() {
		return "set_details_object()";
	}

	@Override
	public Object apply(ActionContext ctx, final GraphObject entity, final Object[] sources) {

		if (sources != null && sources.length == 1) {

			if (sources[0] instanceof GraphObject) {
				((RenderContext) ctx).setDetailsDataObject((GraphObject)sources[0]);
			}

			return "";
		}

		return usage(ctx.isJavaScriptContext());
	}

	@Override
	public String usage(boolean inJavaScriptContext) {
		return (inJavaScriptContext ? ERROR_MESSAGE_SET_DETAILS_OBJECT_JS : ERROR_MESSAGE_SET_DETAILS_OBJECT);
	}

	@Override
	public String shortDescription() {
		return "Adds the given header field and value to the response of the current rendering run";
	}

}
