/**
 * Copyright (C) 2010-2013 Axel Morgner, structr <structr@structr.org>
 *
 * This file is part of structr <http://structr.org>.
 *
 * structr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * structr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with structr.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.structr.websocket.command;

import org.structr.common.PropertyView;
import org.structr.common.RelType;
import org.structr.core.GraphObject;
import org.structr.core.entity.AbstractNode;
import org.structr.core.entity.AbstractRelationship;
import org.structr.websocket.message.WebSocketMessage;

//~--- JDK imports ------------------------------------------------------------

import java.util.*;
import org.structr.web.entity.dom.DOMNode;
import org.structr.websocket.StructrWebSocket;

//~--- classes ----------------------------------------------------------------

/**
 * Websocket command to return the children of the given DOM node
 *
 * @author Axel Morgner
 */
public class DOMNodeChildrenCommand extends AbstractCommand {
	
	static {
		
		StructrWebSocket.addCommand(DOMNodeChildrenCommand.class);

	}

	@Override
	public void processMessage(WebSocketMessage webSocketData) {

		DOMNode node = getDOMNode(webSocketData.getId());

		if (node == null) {

			return;
		}
		
		List<GraphObject> result          = new LinkedList<GraphObject>();
		DOMNode currentNode = (DOMNode) node.getFirstChild();
		
		while (currentNode != null) {

			result.add(currentNode);
			
			currentNode = (DOMNode) currentNode.getNextSibling();

		}

		webSocketData.setView(PropertyView.Ui);
		webSocketData.setResult(result);

		// send only over local connection
		getWebSocket().send(webSocketData, true);

	}

	//~--- get methods ----------------------------------------------------

	@Override
	public String getCommand() {

		return "DOM_NODE_CHILDREN";

	}

}