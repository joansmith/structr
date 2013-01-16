package org.structr.web.entity.dom;


import org.structr.web.common.DOMTest;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 *
 * @author Christian Morgner
 */
public class DOMNodeTest extends DOMTest {

	public void testGetParentNode() {
		
		Document document = getDocument();
		assertNotNull(document);

		Text content = document.createTextNode("Dies ist ein Test");
		assertNotNull(content);
		
		Element div = document.createElement("div");
		assertNotNull(div);
		
		// add child
		div.appendChild(content);

		Node parent = content.getParentNode();
		
		assertEquals(div, parent);

	}
	
	public void testGetChildNodes() {
		
		Document document = getDocument();
		assertNotNull(document);

		Text test1 = document.createTextNode("test1");
		Text test2 = document.createTextNode("test2");
		Text test3 = document.createTextNode("test3");
		assertNotNull(test1);
		assertNotNull(test2);
		assertNotNull(test3);
		
		Element div = document.createElement("div");
		assertNotNull(div);
		
		// add children
		div.appendChild(test1);
		div.appendChild(test2);
		div.appendChild(test3);

		NodeList children = div.getChildNodes();
		
		assertEquals(test1, children.item(0));
		assertEquals(test2, children.item(1));
		assertEquals(test3, children.item(2));
		
	}
	
	public void testRemoveChildNode() {
	
		Document document = getDocument();
		assertNotNull(document);

		Text test1 = document.createTextNode("test1");
		Text test2 = document.createTextNode("test2");
		Text test3 = document.createTextNode("test3");
		Text test4 = document.createTextNode("test4");
		Text test5 = document.createTextNode("test5");
		Text test6 = document.createTextNode("test6");
		assertNotNull(test1);
		assertNotNull(test2);
		assertNotNull(test3);
		assertNotNull(test4);
		assertNotNull(test5);
		assertNotNull(test6);
		
		Element div = document.createElement("div");
		assertNotNull(div);
		
		// add children
		div.appendChild(test1);
		div.appendChild(test2);
		div.appendChild(test3);
		div.appendChild(test4);
		div.appendChild(test5);
		
		// note that we do NOT add test6 as a child!

		NodeList children1 = div.getChildNodes();
		assertEquals(test1, children1.item(0));
		assertEquals(test2, children1.item(1));
		assertEquals(test3, children1.item(2));
		assertEquals(test4, children1.item(3));
		assertEquals(test5, children1.item(4));
		
		// test remove child node method
		div.removeChild(test3);
		
		NodeList children2 = div.getChildNodes();
		assertEquals(test1, children2.item(0));
		assertEquals(test2, children2.item(1));
		assertEquals(test4, children2.item(2));
		assertEquals(test5, children2.item(3));
				
		// test remove child node method
		div.removeChild(test1);
		
		NodeList children3 = div.getChildNodes();
		assertEquals(test2, children3.item(0));
		assertEquals(test4, children3.item(1));
		assertEquals(test5, children3.item(2));
		
		
		// and finally, test errors that should be raised
		try {
			
			div.removeChild(test6);
		
			fail("Removing a node that is not a child of the given node should raise a DOMException");
			
		} catch (DOMException dex) {
			
			assertEquals(DOMException.NOT_FOUND_ERR, dex.code);
		}
		
	}
	
	public void testSiblingMethods() {
		
		Document document = getDocument();
		assertNotNull(document);

		Text test1 = document.createTextNode("test1");
		Text test2 = document.createTextNode("test2");
		Text test3 = document.createTextNode("test3");
		Text test4 = document.createTextNode("test4");
		Text test5 = document.createTextNode("test5");

		assertNotNull(test1);
		assertNotNull(test2);
		assertNotNull(test3);
		assertNotNull(test4);
		assertNotNull(test5);
		
		Element div = document.createElement("div");
		assertNotNull(div);
		
		// add children
		div.appendChild(test1);
		div.appendChild(test2);
		div.appendChild(test3);
		div.appendChild(test4);
		div.appendChild(test5);

		
		// test first child
		assertEquals(test1, div.getFirstChild());
		
		// test last child
		assertEquals(test5, div.getLastChild());
		
		// test sibling methods
		assertNull(test1.getPreviousSibling());
		assertEquals(test3, test4.getPreviousSibling());
		assertEquals(test2, test1.getNextSibling());
		assertNull(test5.getNextSibling());
	}
	
	public void testAppendChildErrors() {
		
		
		Document wrongDocument = getDocument();
		Document document      = getDocument();

		assertNotNull(document);
		assertNotNull(wrongDocument);

		
		Text wrongTextNode = wrongDocument.createTextNode("test");
		
		Text test1 = document.createTextNode("test1");
		Text test2 = document.createTextNode("test2");
		Text test3 = document.createTextNode("test3");
		assertNotNull(test1);
		assertNotNull(test2);
		assertNotNull(test3);
		
		Element div = document.createElement("div");
		assertNotNull(div);
		
		// add children
		div.appendChild(test1);
		div.appendChild(test2);
		div.appendChild(test3);

		NodeList children = div.getChildNodes();
		
		assertEquals(test1, children.item(0));
		assertEquals(test2, children.item(1));
		assertEquals(test3, children.item(2));

		
		Element div2 = document.createElement("div");
		assertNotNull(div2);
		
		div.appendChild(div2);
		
		assertEquals(div, div2.getParentNode());
		
		try {
			
			div.appendChild(wrongTextNode);
			fail("Adding a node that was not created using the correct document should raise a DOMException");
			
		} catch (DOMException dex) {
			
			assertEquals(DOMException.WRONG_DOCUMENT_ERR, dex.code);
		}
		
		
		try {
			
			div.appendChild(div);
			fail("Adding a node to itself should raise a DOMException");
			
		} catch (DOMException dex) {
			
			assertEquals(DOMException.HIERARCHY_REQUEST_ERR, dex.code);
		}
		
		
		try {
			
			div2.appendChild(div);
			fail("Adding one of its own ancestors to a node should raise a DOMException");
			
		} catch (DOMException dex) {
			
			assertEquals(DOMException.HIERARCHY_REQUEST_ERR, dex.code);
		}
	}
	
	public void testReplaceChild() {
		
		Document document = getDocument();
		assertNotNull(document);

		Text test1 = document.createTextNode("test1");
		Text test2 = document.createTextNode("test2");
		Text test3 = document.createTextNode("test3");
		Text test4 = document.createTextNode("test4");
		Text test5 = document.createTextNode("test5");
		Text test6 = document.createTextNode("test5");
		assertNotNull(test1);
		assertNotNull(test2);
		assertNotNull(test3);
		assertNotNull(test4);
		assertNotNull(test5);
		assertNotNull(test6);
		
		Element div = document.createElement("div");
		assertNotNull(div);
		
		// add children
		div.appendChild(test1);
		div.appendChild(test2);
		div.appendChild(test3);
		div.appendChild(test4);
		div.appendChild(test5);

		// examine children
		NodeList children1 = div.getChildNodes();
		assertEquals(test1, children1.item(0));
		assertEquals(test2, children1.item(1));
		assertEquals(test3, children1.item(2));
		assertEquals(test4, children1.item(3));
		assertEquals(test5, children1.item(4));

		
		// test replace child
		div.replaceChild(test6, test3);

		// examine children
		NodeList children2 = div.getChildNodes();
		assertEquals(test1, children2.item(0));
		assertEquals(test2, children2.item(1));
		assertEquals(test6, children2.item(2));
		assertEquals(test4, children2.item(3));
		assertEquals(test5, children2.item(4));
		
	}
	
	public void testReplaceChildWithFragment() {
		
		Document document                     = getDocument();
		org.w3c.dom.DocumentFragment fragment = document.createDocumentFragment();

		assertNotNull(document);
		assertNotNull(fragment);
		
		Text test1 = document.createTextNode("test1");
		Text test2 = document.createTextNode("test2");
		Text test3 = document.createTextNode("test3");
		Text test4 = document.createTextNode("test4");
		Text test5 = document.createTextNode("test5");
		Text test6 = document.createTextNode("test6");
		Text test7 = document.createTextNode("test7");
		Text test8 = document.createTextNode("test8");
		Text test9 = document.createTextNode("test9");
		assertNotNull(test1);
		assertNotNull(test2);
		assertNotNull(test3);
		assertNotNull(test4);
		assertNotNull(test5);
		assertNotNull(test6);
		assertNotNull(test7);
		assertNotNull(test8);
		assertNotNull(test9);
		
		Element div = document.createElement("div");
		assertNotNull(div);
		
		// add children
		div.appendChild(test1);
		div.appendChild(test2);
		div.appendChild(test3);
		div.appendChild(test4);
		div.appendChild(test5);

		// examine children
		NodeList children1 = div.getChildNodes();
		assertEquals(test1, children1.item(0));
		assertEquals(test2, children1.item(1));
		assertEquals(test3, children1.item(2));
		assertEquals(test4, children1.item(3));
		assertEquals(test5, children1.item(4));

		// add children to document fragment
		fragment.appendChild(test6);
		fragment.appendChild(test7);
		fragment.appendChild(test8);
		fragment.appendChild(test9);
		
		// test replace child
		div.replaceChild(fragment, test3);

		// examine children
		NodeList children2 = div.getChildNodes();

		assertEquals(test1, children2.item(0));
		assertEquals(test2, children2.item(1));
		assertEquals(test6, children2.item(2));
		assertEquals(test7, children2.item(3));
		assertEquals(test8, children2.item(4));
		assertEquals(test9, children2.item(5));
		assertEquals(test4, children2.item(6));
		assertEquals(test5, children2.item(7));
		
	}
	
	public void testInsertBefore() {
		
		Document document = getDocument();
		assertNotNull(document);

		Text test1 = document.createTextNode("test1");
		Text test2 = document.createTextNode("test2");
		Text test3 = document.createTextNode("test3");
		Text test4 = document.createTextNode("test4");
		Text test5 = document.createTextNode("test5");
		Text test6 = document.createTextNode("test5");
		assertNotNull(test1);
		assertNotNull(test2);
		assertNotNull(test3);
		assertNotNull(test4);
		assertNotNull(test5);
		assertNotNull(test6);
		
		Element div = document.createElement("div");
		assertNotNull(div);
		
		// add children
		div.appendChild(test1);
		div.appendChild(test2);
		div.appendChild(test3);
		div.appendChild(test4);
		div.appendChild(test5);

		// examine children
		NodeList children1 = div.getChildNodes();
		assertEquals(test1, children1.item(0));
		assertEquals(test2, children1.item(1));
		assertEquals(test3, children1.item(2));
		assertEquals(test4, children1.item(3));
		assertEquals(test5, children1.item(4));

		
		// test replace child
		div.insertBefore(test6, test3);

		// examine children
		NodeList children2 = div.getChildNodes();
		assertEquals(test1, children2.item(0));
		assertEquals(test2, children2.item(1));
		assertEquals(test6, children2.item(2));
		assertEquals(test3, children2.item(3));
		assertEquals(test4, children2.item(4));
		assertEquals(test5, children2.item(5));
		
	}
	
	public void testInsertBeforeWithFragment() {
		
		Document document                     = getDocument();
		org.w3c.dom.DocumentFragment fragment = document.createDocumentFragment();

		assertNotNull(document);
		assertNotNull(fragment);
		
		Text test1 = document.createTextNode("test1");
		Text test2 = document.createTextNode("test2");
		Text test3 = document.createTextNode("test3");
		Text test4 = document.createTextNode("test4");
		Text test5 = document.createTextNode("test5");
		Text test6 = document.createTextNode("test6");
		Text test7 = document.createTextNode("test7");
		Text test8 = document.createTextNode("test8");
		Text test9 = document.createTextNode("test9");
		assertNotNull(test1);
		assertNotNull(test2);
		assertNotNull(test3);
		assertNotNull(test4);
		assertNotNull(test5);
		assertNotNull(test6);
		assertNotNull(test7);
		assertNotNull(test8);
		assertNotNull(test9);
		
		Element div = document.createElement("div");
		assertNotNull(div);
		
		// add children
		div.appendChild(test1);
		div.appendChild(test2);
		div.appendChild(test3);
		div.appendChild(test4);
		div.appendChild(test5);

		// examine children
		NodeList children1 = div.getChildNodes();
		assertEquals(test1, children1.item(0));
		assertEquals(test2, children1.item(1));
		assertEquals(test3, children1.item(2));
		assertEquals(test4, children1.item(3));
		assertEquals(test5, children1.item(4));

		// add children to document fragment
		fragment.appendChild(test6);
		fragment.appendChild(test7);
		fragment.appendChild(test8);
		fragment.appendChild(test9);
		
		// test replace child
		div.insertBefore(fragment, test3);

		// examine children
		NodeList children2 = div.getChildNodes();

		assertEquals(test1, children2.item(0));
		assertEquals(test2, children2.item(1));
		assertEquals(test6, children2.item(2));
		assertEquals(test7, children2.item(3));
		assertEquals(test8, children2.item(4));
		assertEquals(test9, children2.item(5));
		assertEquals(test3, children2.item(6));
		assertEquals(test4, children2.item(7));
		assertEquals(test5, children2.item(8));
		
	}
}