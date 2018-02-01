package ca.bc.gov.nrs.dm.microservice.api.impl;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.nrs.dm.rest.client.v1.DocumentManagementException;
import ca.bc.gov.nrs.dm.rest.client.v1.DocumentManagementService;
import ca.bc.gov.nrs.dm.rest.client.v1.ForbiddenAccessException;
import ca.bc.gov.nrs.dm.rest.client.v1.ValidationException;
import ca.bc.gov.nrs.dm.rest.v1.resource.AbstractFolderResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.AttachmentResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.AttachmentsResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.EndpointsResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.EngagementFolderResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.FileResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.FilesResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.FolderContentResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.FolderResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.RelatedItemResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.RelatedItemsResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.RevisionResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.RevisionsResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.SoftLinkResource;
import ca.bc.gov.nrs.dm.rest.v1.resource.SoftLinksResource;

public class MockDocumentManagementService implements DocumentManagementService {

	private FolderContentResource folderContentResource = null;
	private FolderResource folderResource = null;
	private RevisionResource revisionResource = null;
	private boolean forbiddenError = false;
	private boolean dmsError = false;
	private FileResource fileResource = null;
	private byte[] fileContent = null;
	
	public void setFolderContentResource(FolderContentResource folderContentResource) {
		this.folderContentResource = folderContentResource;
	}
	
	public void setFolderResource(FolderResource folderResource) {
		this.folderResource = folderResource;
	}
	
	public void setForbiddenError(boolean forbiddenError) {
		this.forbiddenError = forbiddenError;
	}
	
	public void setDmsError(boolean dmsError) {
		this.dmsError = dmsError;
	}
	
	public void setFileResource(FileResource fileResource) {
		this.fileResource = fileResource;
	}
	
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	
	public void setRevisionResource(RevisionResource revisionResource) {
		this.revisionResource = revisionResource;
	}
	
	@Override
	public AbstractFolderResource getFolderByPath(String arg0)
			throws DocumentManagementException, ForbiddenAccessException {		
		return folderResource;
	}
	
	@Override
	public FolderContentResource browseFolder(AbstractFolderResource arg0, Integer arg1, Integer arg2)
			throws DocumentManagementException, ForbiddenAccessException {
		
		if(forbiddenError) {
			throw new ForbiddenAccessException("ForbiddenAccess");
		}
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
		
		
		return this.folderContentResource;
	}
	
	@Override
	public FileResource getFileByID(String arg0) throws DocumentManagementException, ForbiddenAccessException {
		if(forbiddenError) {
			throw new ForbiddenAccessException("ForbiddenAccess");
		}
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
		
		return fileResource;
	}
	
	@Override
	public byte[] getFileContent(String arg0) throws DocumentManagementException, ForbiddenAccessException {
		
		if(forbiddenError) {
			throw new ForbiddenAccessException("ForbiddenAccess");
		}
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
		
		return this.fileContent;
	}

	
	@Override
	public void deleteFolder(AbstractFolderResource arg0) throws DocumentManagementException, ForbiddenAccessException {
		if(forbiddenError) {
			throw new ForbiddenAccessException("ForbiddenAccess");
		}
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
	}
	
	@Override
	public FileResource createFile(String arg0, AbstractFolderResource arg1, String arg2, String arg3, String arg4,
			String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12,
			String arg13) throws DocumentManagementException {
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
		
		return this.fileResource;
	}
	
	@Override
	public FolderContentResource browseFolderByID(String arg0)
			throws DocumentManagementException, ForbiddenAccessException {
		
		if(forbiddenError) {
			throw new ForbiddenAccessException("ForbiddenAccess");
		}
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
		
		return this.folderContentResource;
	}
	
	@Override
	public EndpointsResource getTopLevelEndpoints() throws DocumentManagementException {
		return null;
	}

	@Override
	public AbstractFolderResource getFolderByUri(String arg0)
			throws DocumentManagementException, ForbiddenAccessException {
		return null;
	}



	@Override
	public AbstractFolderResource getFolderByID(String arg0)
			throws DocumentManagementException, ForbiddenAccessException {
		if(forbiddenError) {
			throw new ForbiddenAccessException("ForbiddenAccess");
		}
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
		
		return this.folderContentResource;
	}



	@Override
	public FolderContentResource browseFolder(AbstractFolderResource arg0)
			throws DocumentManagementException, ForbiddenAccessException {

		return null;
	}

	@Override
	public FolderContentResource browseFolderByID(String arg0, Integer arg1, Integer arg2)
			throws DocumentManagementException, ForbiddenAccessException {
		if(forbiddenError) {
			throw new ForbiddenAccessException("ForbiddenAccess");
		}
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
		
		return this.folderContentResource;
	}

	@Override
	public FileResource updateFileMetadata(FileResource arg0)
			throws DocumentManagementException, ValidationException, ForbiddenAccessException {

		if(forbiddenError) {
			throw new ForbiddenAccessException("ForbiddenAccess");
		}
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
		
		return this.fileResource;
	}

	@Override
	public <T extends AbstractFolderResource> AbstractFolderResource createFolder(AbstractFolderResource arg0, T arg1)
			throws DocumentManagementException, ValidationException, ForbiddenAccessException {
		return null;
	}


	@Override
	public AbstractFolderResource getFolderMetadata(AbstractFolderResource arg0)
			throws DocumentManagementException, ForbiddenAccessException {
		
		return null;
	}

	@Override
	public <T extends AbstractFolderResource> AbstractFolderResource updateFolderMetadata(T arg0)
			throws DocumentManagementException, ValidationException, ForbiddenAccessException {
		
		return null;
	}

	@Override
	public <T extends AbstractFolderResource> EngagementFolderResource createEngagementFolder(T arg0,
			EngagementFolderResource arg1)
			throws DocumentManagementException, ValidationException, ForbiddenAccessException {
		
		return null;
	}

	@Override
	public EngagementFolderResource updateEngagementFolder(EngagementFolderResource arg0)
			throws DocumentManagementException, ValidationException, ForbiddenAccessException {
		
		return null;
	}

	@Override
	public EngagementFolderResource getEngagementFolder(EngagementFolderResource arg0)
			throws DocumentManagementException, ForbiddenAccessException {
		
		return null;
	}

	@Override
	public EngagementFolderResource getEngagementFolderByID(String arg0)
			throws DocumentManagementException, ForbiddenAccessException {
		
		return null;
	}

	@Override
	public FileResource getFile(FileResource arg0) throws DocumentManagementException {

		return null;
	}

	@Override
	public FileResource getFileByUri(String arg0) throws DocumentManagementException, ForbiddenAccessException {

		return null;
	}

	

	@Override
	public FileResource moveFile(String arg0) throws DocumentManagementException {

		return null;
	}

	@Override
	public FileResource checkoutFile(String arg0) throws DocumentManagementException, ForbiddenAccessException {

		return null;
	}

	@Override
	public FileResource undoCheckoutFile(String arg0) throws DocumentManagementException, ForbiddenAccessException {

		return null;
	}

	@Override
	public void deleteFile(String arg0) throws DocumentManagementException, ForbiddenAccessException {
				
	}

	@Override
	public FilesResource searchFiles(AbstractFolderResource arg0, String arg1, String arg2, String arg3, String arg4,
			String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12,
			String arg13, String arg14, String arg15, String arg16, Boolean arg17, String arg18, String arg19,
			String arg20, String arg21, String arg22, String arg23, String arg24, Integer arg25, Integer arg26,
			String[] arg27) throws DocumentManagementException {
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
		
		FilesResource filesResource = new FilesResource();
		List<FileResource> fileResourceList = new ArrayList<FileResource>();
		fileResourceList.add(this.fileResource);
		filesResource.setFileList(fileResourceList);
		
		return filesResource;
	}

	@Override
	public FileResource checkinFile(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5,
			String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13)
			throws DocumentManagementException, ForbiddenAccessException {
		
		if(forbiddenError) {
			throw new ForbiddenAccessException("ForbiddenAccess");
		}
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
		
		return this.fileResource;
	}



	@Override
	public RevisionsResource getRevisions(FileResource arg0, Integer arg1, Integer arg2)
			throws DocumentManagementException {
		
		if(dmsError) {
			throw new DocumentManagementException("DocumentManagementException");
		}
		
		RevisionsResource revisionsResource = new RevisionsResource();
		
		List<RevisionResource> revisionResourceList = new ArrayList<RevisionResource>();
		revisionResourceList.add(this.revisionResource);
		revisionsResource.setRevisionResources(revisionResourceList);
	
		return revisionsResource;
	}

	@Override
	public RevisionsResource getRevisionsByFileID(String arg0, Integer arg1, Integer arg2)
			throws DocumentManagementException, ForbiddenAccessException {
		
		return null;
	}

	@Override
	public FileResource getRevisionMetadata(RevisionResource arg0) throws DocumentManagementException {
		
		return null;
	}

	@Override
	public FileResource getRevisionMetadataByID(String arg0, String arg1) throws DocumentManagementException {
		
		return null;
	}

	@Override
	public byte[] getRevisionContent(RevisionResource arg0) throws DocumentManagementException {
		
		return null;
	}

	@Override
	public byte[] getRevisionContentByID(String arg0, String arg1) throws DocumentManagementException {
		
		return null;
	}

	@Override
	public void hardDeleteRevision(RevisionsResource arg0)
			throws DocumentManagementException, ForbiddenAccessException {
		
		
	}

	@Override
	public SoftLinksResource getSoftLinks(FileResource arg0, Integer arg1, Integer arg2)
			throws DocumentManagementException {
		
		return null;
	}

	@Override
	public SoftLinksResource getSoftLinksByFileID(String arg0, Integer arg1, Integer arg2)
			throws DocumentManagementException {
		
		return null;
	}

	@Override
	public SoftLinksResource createSoftLink(String arg0, String arg1) throws DocumentManagementException {
		
		return null;
	}

	@Override
	public void deleteSoftLink(SoftLinkResource arg0) throws DocumentManagementException, ForbiddenAccessException {
		
		
	}

	@Override
	public AttachmentsResource getAttachments(FileResource arg0, Integer arg1, Integer arg2)
			throws DocumentManagementException {
		
		return null;
	}

	@Override
	public AttachmentsResource getAttachmentsByFileID(String arg0, Integer arg1, Integer arg2)
			throws DocumentManagementException {
		
		return null;
	}

	@Override
	public AttachmentsResource getAttachmentsForRevision(RevisionResource arg0, Integer arg1, Integer arg2)
			throws DocumentManagementException {
		
		return null;
	}

	@Override
	public AttachmentsResource getAttachmentsByRevisionID(String arg0, String arg1, Integer arg2, Integer arg3)
			throws DocumentManagementException {
		
		return null;
	}

	@Override
	public void deleteAttachment(AttachmentResource arg0) throws DocumentManagementException, ForbiddenAccessException {
		
		
	}

	@Override
	public AttachmentResource getAttachmentContent(AttachmentResource arg0)
			throws DocumentManagementException, ForbiddenAccessException {
		
		return null;
	}

	@Override
	public RelatedItemsResource getRelatedItems(FileResource arg0, Integer arg1, Integer arg2, String[] arg3)
			throws DocumentManagementException {
		
		return null;
	}

	@Override
	public RelatedItemResource createCrossReferenceRelationship(FileResource arg0, FileResource arg1, String arg2)
			throws DocumentManagementException, ValidationException, ForbiddenAccessException {
		
		return null;
	}

	@Override
	public RelatedItemResource createSupportedByRelationship(FileResource arg0, FileResource arg1, String arg2)
			throws DocumentManagementException, ValidationException, ForbiddenAccessException {
		
		return null;
	}

	@Override
	public RelatedItemResource createSupportsRelationship(FileResource arg0, FileResource arg1, String arg2)
			throws DocumentManagementException, ValidationException, ForbiddenAccessException {
		
		return null;
	}

	@Override
	public void deleteRelatedItem(RelatedItemResource arg0)
			throws DocumentManagementException, ForbiddenAccessException {
		
		
	}

}
