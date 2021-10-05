package ru.sbsoft.server.services;

import ru.sbsoft.server.services.SBFRemoteServiceServlet;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.dao.IKLADRDao;
import ru.sbsoft.shared.kladr.KLADRAddressDict;
import ru.sbsoft.shared.kladr.KLADRItem;
import ru.sbsoft.shared.kladr.SearchModel;
import ru.sbsoft.shared.services.IKLADRService;
import ru.sbsoft.shared.services.ServiceConst;

/**
 * @author balandin
 * @since Mar 15, 2013 5:35:14 PM
 */
@WebServlet(urlPatterns = {ServiceConst.KLADR_SERVICE_LONG})
public class KLADRService extends SBFRemoteServiceServlet implements IKLADRService {

    @EJB
    private IKLADRDao kladrDao;

    @Override
    public List<SearchModel> search(String query, boolean actualStrict) {
        return kladrDao.search(query, actualStrict);
    }

    @Override
    public KLADRAddressDict address(int scoreDoc) {
        return kladrDao.address(scoreDoc);
    }

    @Override
    public List<KLADRItem> lookup(String[] codes, boolean actualStrict) {
        return kladrDao.lookup(codes, actualStrict);
    }

    @Override
    public String postcode(String[] codes, String house, String building) {
        return kladrDao.postcode(codes, house, building);
    }
}
