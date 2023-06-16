//import com.ing.rtpe.amsinterfacefeedback00100105.CabiXmlBookRspns;
//import com.ing.rtpe.bor.integration.endpoints.TestBorEmsSender;
//import com.ing.rtpe.bor.integration.stub.FeedbackListenerStub;
//import com.ing.rtpe.bor.integration.stub.GbsBookingListenerStub;
//import com.ing.rtpe.bor.integration.stub.GbsBookingListenerStub.GbsBookResponseGenerator;
//import com.ing.rtpe.bor.model.TransactionStatus;
//import com.ing.rtpe.bor.utils.BorDateUtil;
//import com.ing.rtpe.bor01.BorEvent;
//import com.ing.rtpe.bor01.BorProcessStateInput;
//import com.ing.rtpe.fsm.workflow.model.WorkflowExecution;
//import com.ing.rtpe.fsm.workflow.repository.WorkflowExecutionRepository;
//import iso.std.iso._20022.tech.xsd.camt_054_001.DateAndDateTime2Choice;
//import lombok.extern.slf4j.Slf4j;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.io.InputStream;
//import java.time.Duration;
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//import static com.ing.rtpe.bor.integration.stub.EventMonitor.EventMatcher;
//import static com.ing.rtpe.bor.queue.tibcoems.TibcoEmsQueue.DVT_BOOK_REQUEST;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.awaitility.Awaitility.with;
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
//
//
//@Slf4j
//@ActiveProfiles("test")
//@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "spring.config.location=conf/,src/test/resources/")
//@Import(E2ETestConfig.class)
//public class GbsBookingE2ETest {
//
//    @Autowired
//    TestBorEmsSender testBorEmsSender;
//
//    @Autowired
//    FeedbackListenerStub feedbackListenerStub;
//
//    @Autowired
//    GbsBookingListenerStub gbsBookingListenerStub;
//
//    @Autowired
//    WorkflowExecutionRepository workflowExecutionRepository;
//
//    final Map<String, GbsBookResponseGenerator> gbsBookResponseGenerators = Map.of(
//            "SUCCESS", this::createSuccessGBSBookingResponseEvent,
//            "REJECT", this::createRjctAB05GBSBookingResponseEvent,
//            "REVOLVE", this::createRevolveGBSBookingResponseEvent
//            //"RJCT_AM06", null
//    );
//
//
//    @BeforeEach
//    void setup() {
//        gbsBookingListenerStub.clearResponseGenerators();
//        feedbackListenerStub.eventMonitor.reset(5000);
//    }
//
//
//    @DisplayName("GBS Booking Flow test cases")
//    @ParameterizedTest
//    @CsvSource({
//            "Happy Flow, gbs_booking/happy_flow.xml, SUCCESS, PENDING-COMPLETED, 1-3-5-7, NEW-Booking-Notify_Completed-FINISHED, OK-OK-OK-FINAL",
//            "Happy Flow, gbs_booking/happy_flow.xml, REJECT, PENDING-REJECTED, 1-3-5-7, NEW-Booking-Notify_Reject-REJECTED, OK-RJCT-OK-FINAL"
//    })
//    public void testGBSBookingFlow(String scenarioName, String fileName, String bookRespType,
//                                   String feedbackStatus, String seqIds, String fsmStates, String fsmResults) throws Exception {
//
//        log.info("Executing test for GBS booking scenario {}", scenarioName);
//
//        String txnId = UUID.randomUUID().toString();
//
//        gbsBookingListenerStub.registerResponseGenerator(txnId, gbsBookResponseGenerators.get(bookRespType));
//
//        var feedbackStatusList = Arrays.stream(feedbackStatus.split("-")).map(s -> TransactionStatus.valueOf(s)).collect(Collectors.toList());
//        for (var status: feedbackStatusList) {
//            feedbackListenerStub.eventMonitor.addWatch(createEventMatcher(txnId, status), 1);
//        }
//
//        String xml = new String(getResourceAsStream(fileName).readAllBytes(), "UTF-8");
//        xml = enrichBookingRequest(xml, txnId);
//        testBorEmsSender.send(DVT_BOOK_REQUEST, xml);
//
//        with().pollInterval(Duration.ofMillis(100))
//                .await()
//                .atMost(Duration.ofSeconds(1))
//                .untilAsserted(() -> {
//                    var feedbacks = feedbackListenerStub.eventMonitor.getAllLogs();
//                    var receivedFeedbackCodes = feedbacks.stream().map(fb -> fb.getBookRspn().get(0).getSts()).collect(Collectors.toList());
//                    var expectedFeedbackCodes = feedbackStatusList.stream().map(f -> f.getValue()).collect(Collectors.toList());
//                    assertThat(expectedFeedbackCodes).containsAll(receivedFeedbackCodes);
//                    List<WorkflowExecution> fsmHistoryItems =  workflowExecutionRepository.findAllWorkflowExecutionById(txnId);
//                    assertThat(fsmHistoryItems).extracting("sequenceId").containsExactly(Arrays.stream(seqIds.split("-")).map(e -> Integer.valueOf(e)).toArray());
//                    assertThat(fsmHistoryItems).extracting("state").containsExactly(fsmStates.split("-"));
//                    assertThat(fsmHistoryItems).extracting("result").containsExactly(fsmResults.split("-"));
//                });
//    }
//
//    @Nullable
//    private static InputStream getResourceAsStream(String inputRequest) {
//        return GbsBookingE2ETest.class.getClassLoader().getResourceAsStream("cabi/" + inputRequest);
//    }
//
//
//    private String enrichBookingRequest(String xml, String txnId) {
//        DateAndDateTime2Choice targetDateTime2Choice = new DateAndDateTime2Choice();
//        targetDateTime2Choice.setDt(BorDateUtil.convertToXMLGregorianCalendar(LocalDate.now()));
//        targetDateTime2Choice.setDtTm(BorDateUtil.convertToXMLGregorianCalendar(LocalDate.now()));
//
//        return xml.replace("{MsgId}", UUID.randomUUID().toString())
//                .replace("{CreDtTm}", targetDateTime2Choice.getDtTm().toXMLFormat())
//                .replace("{INGTxId}", "ING-" + txnId)
//                .replace("{NtryRef}", txnId)
//                .replace("{TxId}", txnId)
//                .replace("{OrgnlTxId}", "Org-" + txnId)
//                .replace("{ValDt}", targetDateTime2Choice.getDt().toXMLFormat());
//    }
//
//    private BorEvent createSuccessGBSBookingResponseEvent(BorProcessStateInput processStateInput) {
//        BorEvent event = baseBorEvent(processStateInput);
//        event.setResult("OK");
//        return event;
//    }
//
//    private BorEvent createRjctAB05GBSBookingResponseEvent(BorProcessStateInput processStateInput) {
//        BorEvent event = baseBorEvent(processStateInput);
//        event.setResult("RJCT");
//        event.setReasonCode("AB05");
//        return event;
//    }
//
//    private BorEvent createRevolveGBSBookingResponseEvent(BorProcessStateInput processStateInput) {
//        BorEvent event = baseBorEvent(processStateInput);
//        event.setResult("REVOLVE");
//        event.setReasonCode("AM04");
//        return event;
//    }
//
//    @NotNull
//    private static BorEvent baseBorEvent(BorProcessStateInput processStateInput) {
//        BorEvent event = new BorEvent();
//        event.setCabiTxEntityId(processStateInput.getContext().getCabiTxEntityId());
//        event.setSequenceId(processStateInput.getCurrentSequenceId());
//        event.setSource("GBS_BOOKING_STUB");
//        return event;
//    }
//
//    private EventMatcher<CabiXmlBookRspns> createEventMatcher(String txId, TransactionStatus status) {
//        return (CabiXmlBookRspns feedBacks) -> {
//            if (feedBacks.getBookRspn().size() < 1) {
//                return false;
//            }
//            var ntryRef = feedBacks.getBookRspn().get(0).getNtryRef();
//            var sts = feedBacks.getBookRspn().get(0).getSts();
//            return txId.equals(ntryRef) && status.getValue().equals(sts);
//        };
//    }
//
//
//}
//
//
