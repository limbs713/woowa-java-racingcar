package racingcar;

import camp.nextstep.edu.missionutils.test.NsTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import racingcar.model.repository.CarRepository;
import racingcar.model.service.GameService;

import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomNumberInRangeTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static racingcar.view.OutputView.outputWinner;

class ApplicationTest extends NsTest {
    private static final int MOVING_FORWARD = 4;
    private static final int STOP = 3;

    @Test
    void 전진_정지() {
        assertRandomNumberInRangeTest(
                () -> {
                    run("pobi,woni", "1");
                    assertThat(output()).contains("pobi : -", "woni : ", "최종 우승자 : pobi");
                },
                MOVING_FORWARD, STOP
        );
    }

    @Test
    void 이름에_대한_예외_처리() {
        assertSimpleTest(() ->
                assertThatThrownBy(() -> runException("pobi,javaji", "1"))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("복수의 우승자에 대한 출력 검사")
    void multiWinnerTest() {
        //given
        CarRepository carRepository = new CarRepository();
        GameService gameService = new GameService(carRepository);
        List<String> players = new ArrayList<>(List.of("faker", "chovy", "bdd"));
        gameService.createCars(players);

        //when
        carRepository.findByName("faker").setForward(5);
        carRepository.findByName("chovy").setForward(5);
        carRepository.findByName("bdd").setForward(4);
        //then
        outputWinner(gameService.resultGame());
        assertThat(output()).contains("faker, chovy");
    }
    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
