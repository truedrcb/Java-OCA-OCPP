package core_features

import eu.chargetime.ocpp.test.FakeCentralSystem
import eu.chargetime.ocpp.test.FakeChargePoint
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions;

class MeterValues extends Specification
{
    @Shared FakeCentralSystem centralSystem = FakeCentralSystem.getInstance();
    @Shared FakeChargePoint chargePoint = new FakeChargePoint();

    def setupSpec() {
        // When a Central System is running
        centralSystem.started();
    }

    def setup() {
        chargePoint.connect();
    }

    def cleanup() {
        chargePoint.disconnect();
    }

    def "Charge point sends MeterValues request and receives a response"() {
        def conditions = new PollingConditions(timeout: 1);
        when:
        chargePoint.sendMeterValuesRequest();

        then:
        conditions.eventually {
            centralSystem.hasReceivedMeterValuesRequest();
        }

        when:
            centralSystem.sendMeterValuesConfirmation();

        then:
        conditions.eventually {
            chargePoint.hasReceivedMeterValuesConfirmation();
        }

    }

}
