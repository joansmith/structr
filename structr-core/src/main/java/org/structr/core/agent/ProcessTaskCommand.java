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
package org.structr.core.agent;

/**
 * Hands over one ore more {@link Task}s to the agent service for processing.
 * 
 * @author Christian Morgner
 */
public class ProcessTaskCommand extends AgentServiceCommand {

	public void execute(Task... tasks) {
		
		AgentService agentService = (AgentService)arguments.get("agentService");
		if(agentService != null) {
			
			for(Task task : tasks) {
				
				agentService.processTask(task);
			}
		}
	}
}