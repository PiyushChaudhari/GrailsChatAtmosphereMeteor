<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main"/>
    </head>
    <body>
    	
    	<table>
    		<thead>
    			<tr>
    				<th> User Id</th>
    				<th> User Name</th>
    				<th> Action</th>
    			</tr>
    		</thead>
    		<tbody>
    			
    			<g:each in="${chatUsers}" var="userInstance" >
    				<tr>
	    				<td>${userInstance?.id}</td>
	    				<td>${userInstance?.username}</td>
	    				<td> <g:link target="_blank" controller="chat" action="startChat" id="${userInstance?.id}" >Start Chat</g:link>  </td>
	    			</tr>
    			</g:each>
    		</tbody>
    	</table>
    </body>
</html>