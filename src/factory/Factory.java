package factory;

import serviceimpl.DomainServiceImpl;
import serviceimpl.FileServiceImpl;
import services.IDomainService;
import services.IFileService;

public class Factory {
	private static Factory instance;
    
    private Factory(){}
    private static IFileService fileService;
    private static IDomainService domainService;
     
    public static Factory getInstance(){
        if(instance == null&&fileService==null&&domainService==null){
            instance = new Factory();
            fileService=new FileServiceImpl();
            domainService = new DomainServiceImpl();
        }
        return instance;
    }

	public static IFileService getFileService() {
		return fileService;
	}

	public static IDomainService getDomainService() {
		return domainService;
	}
}
