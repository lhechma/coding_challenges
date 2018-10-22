package com.kuehne_nagel;

import com.kuehne_nagel.model.Contestant;
import com.kuehne_nagel.model.XMLOutput;
import com.kuehne_nagel.model.events.EventFactory;
import com.kuehne_nagel.model.ranking.Ranking;
import com.kuehne_nagel.model.scoring.ScoringStrategyFactory;
import name.falgout.jeffrey.testing.junit5.MockitoExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.aggregator.DefaultArgumentsAccessor;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DecathlonTest {

    private Decathlon decathlon;

    @Mock
    private static XMLOutput output;

    @Captor
    ArgumentCaptor<List<Ranking>> sortedListCapotr;

    @BeforeEach
    public void init() {
        ScoringStrategyFactory scoringStrategyFactory = new ScoringStrategyFactory();
        EventFactory eventFactory = new EventFactory(scoringStrategyFactory);
        decathlon = new Decathlon(eventFactory);
        Mockito.doNothing().when(output).write(sortedListCapotr.capture());
    }

    @Test
    @DisplayName("John Smith scores 3400 and ranks 1st")
    public void testJohnSmithScore() {
        Contestant contestant = from("John Smith;12.61;5.00;9.22;1.50;60.39;16.43;21.60;2.60;35.81;5.25.72");
        decathlon.withAllEvents().withContestant(contestant).getRankings(output);
        List<Ranking> value = sortedListCapotr.getValue();
        assertThat(value.get(0).getScore()).isEqualTo(3400);
        assertThat(value.get(0).getRank()).isEqualTo(1);

    }


    @Test
    @DisplayName("Jane Doe scores 2400 and ranks 1st")
    public void testJaneDoe() {
        Contestant contestant = from("Jane Doe;13.04;4.53;7.79;1.55;64.72;18.74;24.20;2.40;28.20;6.50.76");
        decathlon.withAllEvents().withContestant(contestant).getRankings(output);
        List<Ranking> value = sortedListCapotr.getValue();
        assertThat(value.get(0).getScore()).isEqualTo(2400);
        assertThat(value.get(0).getRank()).isEqualTo(1);

    }

    @Test
    @DisplayName("Jaan Lepp scores 2700 and ranks 1st")
    public void testJaanLepp() {
        Contestant contestant = from("Jaan Lepp;13.75;4.84;10.12;1.50;68.44;19.18;30.85;2.80;33.88;6.22.75");
        decathlon.withAllEvents().withContestant(contestant).getRankings(output);
        List<Ranking> value = sortedListCapotr.getValue();
        assertThat(value.get(0).getScore()).isEqualTo(2700);
        assertThat(value.get(0).getRank()).isEqualTo(1);
    }


    @Test
    @DisplayName("Foo Bar scores 2400 and ranks 1st")
    public void testFooBar() {
        Contestant contestant = from("Foo Bar;13.43;4.35;8.64;1.50;66.06;19.05;24.89;2.20;33.48;6.51.01");
        decathlon.withAllEvents().withContestant(contestant).getRankings(output);
        List<Ranking> value = sortedListCapotr.getValue();
        assertThat(value.get(0).getScore()).isEqualTo(2400);
        assertThat(value.get(0).getRank()).isEqualTo(1);
    }

    @Test
    @DisplayName("Absent scores 0 and ranks 1st if he's alone")
    public void testAbsent() {
        Contestant contestant = from("Absent;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.00.00");
        decathlon.withAllEvents().withContestant(contestant).getRankings(output);
        List<Ranking> value = sortedListCapotr.getValue();
        assertThat(value.get(0).getScore()).isEqualTo(0);
        assertThat(value.get(0).getRank()).isEqualTo(1);
    }

    @Test
    @DisplayName("Picky attempts one event only, scores 400 and ranks 1st if he's alone")
    public void testPicky() {
        Contestant contestant = from("Picky;13.43;0;0;0;0;0;0;0;0;0.00.00");
        decathlon.withAllEvents().withContestant(contestant).getRankings(output);
        List<Ranking> value = sortedListCapotr.getValue();
        assertThat(value.get(0).getScore()).isEqualTo(400);
        assertThat(value.get(0).getRank()).isEqualTo(1);
    }

    @Test
    @DisplayName("John Smith ranks 1st,Jaan Leep  2nd, Jane Doe 3rd, and absent is last with different scores")
    public void testMultipleContestantsWithDifferentScores() {
        Contestant johnSmith = from("John Smith;12.61;5.00;9.22;1.50;60.39;16.43;21.60;2.60;35.81;5.25.72");
        Contestant janeDoe = from("Jane Doe;13.04;4.53;7.79;1.55;64.72;18.74;24.20;2.40;28.20;6.50.76");
        Contestant jaanLepp = from("Jaan Lepp;13.75;4.84;10.12;1.50;68.44;19.18;30.85;2.80;33.88;6.22.75");
        Contestant absent = from("Absent;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.00.00");

        decathlon.withAllEvents().withContestant(janeDoe).
                withContestant(johnSmith).
                withContestant(jaanLepp).
                withContestant(absent).
                getRankings(output);
        List<Ranking> value = sortedListCapotr.getValue();
        assertThat(value.size()).isEqualTo(4);
        assertThat(value.get(0).getContestant()).isEqualTo("John Smith");
        assertThat(value.get(0).getRank()).isEqualTo(1);
        assertThat(value.get(1).getContestant()).isEqualTo("Jaan Lepp");
        assertThat(value.get(1).getRank()).isEqualTo(2);
        assertThat(value.get(2).getContestant()).isEqualTo("Jane Doe");
        assertThat(value.get(2).getRank()).isEqualTo(3);
        assertThat(value.get(3).getContestant()).isEqualTo("Absent");
        assertThat(value.get(3).getRank()).isEqualTo(4);
    }

    @Test
    @DisplayName("John Smith ranks 1st,Jaan Leep  2nd, Jane Doe and foo bar 3rd-4th, and absent is last with different scores")
    public void testMultipleContestantsWithSomeHavingTheSameScore() {
        Contestant johnSmith = from("John Smith;12.61;5.00;9.22;1.50;60.39;16.43;21.60;2.60;35.81;5.25.72");
        Contestant janeDoe = from("Jane Doe;13.04;4.53;7.79;1.55;64.72;18.74;24.20;2.40;28.20;6.50.76");
        Contestant jaanLepp = from("Jaan Lepp;13.75;4.84;10.12;1.50;68.44;19.18;30.85;2.80;33.88;6.22.75");
        Contestant absent = from("Absent;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.00.00");
        Contestant fooBar = from("Foo Bar;13.43;4.35;8.64;1.50;66.06;19.05;24.89;2.20;33.48;6.51.01");
        decathlon.withAllEvents().withContestant(janeDoe).
                withContestant(johnSmith).
                withContestant(jaanLepp).
                withContestant(fooBar).
                withContestant(absent).getRankings(output);
        List<Ranking> value = sortedListCapotr.getValue();
        assertThat(value.size()).isEqualTo(4);
        assertThat(value.get(0).getContestant()).isEqualTo("John Smith");
        assertThat(value.get(0).getRank()).isEqualTo(1);
        assertThat(value.get(1).getContestant()).isEqualTo("Jaan Lepp");
        assertThat(value.get(1).getRank()).isEqualTo(2);
        //With a tie, order by name
        assertThat(value.get(2).getContestant()).isEqualTo("Foo Bar-Jane Doe");
        assertThat(value.get(2).getRank()).isEqualTo(3);
        assertThat(value.get(2).getRankAsString()).isEqualTo("3-4");
        assertThat(value.get(3).getContestant()).isEqualTo("Absent");
        assertThat(value.get(3).getRank()).isEqualTo(5);
    }

    @Test
    @DisplayName("Illegal argument exception is thrown if the measurements are out of bounds")
    public void testErroneousMeasurements() {
        Contestant justWrong = from("John Smith;12.61;0.01;9.22;1.50;60.39;16.43;21.60;2.60;35.81;5.25.72");
        Assertions.assertThrows(IllegalArgumentException.class,()->decathlon.withAllEvents().withContestant(justWrong).getRankings(output));
    }


    @Test
    @DisplayName("test writing scores to XML")
    public void testWritingResultsToXML() throws IOException {
        String testOutputFolder = "./target/test-classes/";
        output = new XMLOutput(testOutputFolder+"decathlon");
        Contestant johnSmith = from("John Smith;12.61;5.00;9.22;1.50;60.39;16.43;21.60;2.60;35.81;5.25.72");
        Contestant janeDoe = from("Jane Doe;13.04;4.53;7.79;1.55;64.72;18.74;24.20;2.40;28.20;6.50.76");
        Contestant jaanLepp = from("Jaan Lepp;13.75;4.84;10.12;1.50;68.44;19.18;30.85;2.80;33.88;6.22.75");
        Contestant absent = from("Absent;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.0;0.00.00");
        Contestant fooBar = from("Foo Bar;13.43;4.35;8.64;1.50;66.06;19.05;24.89;2.20;33.48;6.51.01");
        decathlon.withAllEvents().withContestant(janeDoe).
                withContestant(johnSmith).
                withContestant(jaanLepp).
                withContestant(fooBar).
                withContestant(absent).getRankings(output);

        //Could have used xmlunit, this is just for demonstration purposes
        Path outputFilePath = FileSystems.getDefault().getPath(testOutputFolder+"decathlon.xml");
        Path referenceFilePath = FileSystems.getDefault().getPath(testOutputFolder+"reference-decathlon.xml");
        assertThat(Files.exists(outputFilePath)).isTrue();
        byte[] f1 = Files.readAllBytes(outputFilePath);
        byte[] f2 = Files.readAllBytes(referenceFilePath);
        assertThat(f1).isEqualTo(f2);

    }

    private static Contestant from(String input) {
        String[] parts = input.split(";");
        DefaultArgumentsAccessor accessor = new DefaultArgumentsAccessor(parts);
        return (Contestant) new ContestantAggregator().aggregateArguments(accessor, null);
    }

}
