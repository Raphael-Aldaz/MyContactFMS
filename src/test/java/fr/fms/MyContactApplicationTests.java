package fr.fms;

import fr.fms.entities.Category;
import fr.fms.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class MyContactApplicationTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;
    @Test
    void test_add_category() {
        //GIVEN
        Category anonymous = categoryRepository.save(new Category());
        anonymous.setCategoryName("Anonymous");
        Category savedCategory = categoryRepository.save(anonymous);

        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getCategoryName()).isEqualTo("Anonymous");
        //WHEN
    }

    @Test
    public void testFindCategoryById(){
        Category category = new Category();
        category.setCategoryName("Test Category");
        entityManager.persist(category);
        entityManager.flush();

        Category found = categoryRepository.findById(category.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getCategoryName()).isEqualTo("Test Category");
    }

}
