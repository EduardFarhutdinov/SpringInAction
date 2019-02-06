package tacos.data;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;
import tacos.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcTacoRepository(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Taco save(Taco taco)  {
        Long  tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for (Ingredient ingredient : taco.getIngredients()){
            saveIngredientToTaco(ingredient,tacoId);
        }

        return taco;
    }
    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
                        "insert into TACO (name, createdAt) values (?, ?)",
                        Types.VARCHAR, Types.TIMESTAMP
                ).newPreparedStatementCreator(
                        Arrays.asList(
                                taco.getName(),
                                new Timestamp(taco.getCreatedAt().getTime())));

               KeyHolder keyHolder = new GeneratedKeyHolder();


               jdbcTemplate.update(psc,keyHolder);

        System.out.println(keyHolder.getKey().longValue());

        return keyHolder.getKey().longValue();

    }
    private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
          jdbcTemplate.update(
            "insert into TACO_INGREDIENTS (taco, ingredient) " + "values (?, ?)",
            tacoId, ingredient.getId());
    }


}
