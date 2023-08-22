//package test.CascadeType;
//
//import com.vux.security.entity.CheckStatus;
//import com.vux.security.entity.User;
//import com.vux.security.utils.HibernateUtil;
//import org.hibernate.*;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//public class CascadeTypeTest {
//
//    private static SessionFactory sessionFactory;
//    private Session session;
//    private Transaction transaction;
//
//    @BeforeClass
//    public static void beforeTests() {
//        sessionFactory = HibernateUtil.getSessionFactory();
//    }
//
//    @Before
//    public void setUp() {
//        session = sessionFactory.openSession();
//        transaction = session.beginTransaction();
//    }
//
//    @After
//    public void tearDown() {
//        transaction.rollback();
//        session.close();
//    }
//
//    @Test
//    public void whenParentSavedThenChildSaved() {
//        User user = new User();
//        CheckStatus checkStatus = new CheckStatus();
//        checkStatus.setUser(user);
//        user.setCheckStatus(List.of(checkStatus));
//        session.persist(user);
//        session.flush();
//    }
//
//    @Test
//    public void  whenParentSavedThenMerged() {
//        User user = User.builder()
//                .name("user1")
//                .build();
//        CheckStatus checkStatus = CheckStatus.builder()
//                .checkinLate(100L)
//                .build();
//        checkStatus.setUser(user);
//        user.setCheckStatus(List.of(checkStatus));
//        session.persist(user);
//        session.flush();
//        int checkStatusId = checkStatus.getId();
//        session.clear();
//
//
//        CheckStatus savedCheckStatus = session.find(CheckStatus.class, checkStatusId);
//        User savedUser = savedCheckStatus.getUser();
//
//        savedUser.setName("user2");
//        savedCheckStatus.setCheckinLate(50L);
//        session.merge(savedUser);
//        session.flush();
//    }
//
//    @Test
//    public void whenParentRemovedThenChildRemoved() {
//        User user = User.builder()
//                .name("user1")
//                .build();
//        CheckStatus checkStatus = CheckStatus.builder()
//                .checkinLate(100L)
//                .build();
//        checkStatus.setUser(user);
//        user.setCheckStatus(List.of(checkStatus));
//        session.persist(user);
//        session.flush();
//        int userId = user.getId();
//        session.clear();
//
//        User savedUser = session.find(User.class, userId);
//        session.remove(savedUser);
//        session.flush();
//    }
//
//    @Test
//    public void whenParentDetachedThenChildDetached() {
//        User user = User.builder()
//                .name("user1")
//                .build();
//        CheckStatus checkStatus = CheckStatus.builder()
//                .checkinLate(100L)
//                .build();
//        checkStatus.setUser(user);
//        user.setCheckStatus(List.of(checkStatus));
//        session.persist(user);
//        session.flush();
//
//        assertThat(session.contains(user)).isTrue();
//        assertThat(session.contains(checkStatus)).isTrue();
//
//        session.detach(user);
//
//        assertThat(session.contains(user)).isFalse();
//        assertThat(session.contains(checkStatus)).isFalse();
//    }
//
//    @Test
//    public void whenDetachedAndLockedThenBothReattached() {
//        User user = User.builder()
//                .name("user1")
//                .build();
//        CheckStatus checkStatus = CheckStatus.builder()
//                .checkinLate(100L)
//                .build();
//        checkStatus.setUser(user);
//        user.setCheckStatus(List.of(checkStatus));
//        session.persist(user);
//        session.flush();
//
//        assertThat(session.contains(user)).isTrue();
//        assertThat(session.contains(checkStatus)).isTrue();
//
//        session.detach(user);
//
//        assertThat(session.contains(user)).isFalse();
//        assertThat(session.contains(checkStatus)).isFalse();
//
//        session.unwrap(Session.class)
//                .buildLockRequest(new LockOptions(LockMode.NONE))
//                .lock(user);
//
//        assertThat(session.contains(user)).isTrue();
//        assertThat(session.contains(checkStatus)).isTrue();
//    }
//
//
//    @Test
//    public void whenParentRefreshedThenChildRefreshed() {
//        User user = User.builder()
//                .name("user1")
//                .build();
//        CheckStatus checkStatus = CheckStatus.builder()
//                .checkinLate(100L)
//                .build();
//        checkStatus.setUser(user);
//        user.setCheckStatus(List.of(checkStatus));
//        session.persist(user);
//        session.flush();
//
//        user.setName("user2");
//        checkStatus.setCheckinLate(50L);
//
//        session.refresh(user);
//
//        assertThat(user.getName()).isEqualTo("user1");
//        assertThat(checkStatus.getCheckinLate()).isEqualTo(100L);
//    }
//
//}
