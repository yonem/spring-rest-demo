package jp.ne.yonem.restful;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(
    packages = "jp.ne.yonem.restful",
    importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

  /** レイヤー間の依存関係を検証するルール */
  @ArchTest
  static final ArchRule layer_dependencies_are_respected =
      layeredArchitecture()
          .consideringAllDependencies()
          .layer("Controller")
          .definedBy("..presentation.controller..")
          .layer("Service")
          .definedBy("..application..")
          .layer("Infrastructure")
          .definedBy("..infrastructure..")
          .whereLayer("Controller")
          .mayNotBeAccessedByAnyLayer()
          .whereLayer("Service")
          .mayOnlyBeAccessedByLayers("Controller");

  /** Serviceアノテーションがついたクラスの命名規則を検証 */
  @ArchTest
  static final ArchRule services_should_be_named_correctly =
      classes()
          .that()
          .areAnnotatedWith(Service.class)
          .and()
          .areNotEnums()
          .should()
          .haveSimpleNameEndingWith("Service");

  /** RestControllerアノテーションがついたクラスの命名規則を検証 */
  @ArchTest
  static final ArchRule controllers_should_be_named_correctly =
      classes()
          .that()
          .areAnnotatedWith(RestController.class)
          .and()
          .areNotEnums()
          .should()
          .haveSimpleNameEndingWith("Controller");

  /** presentation.controllerパッケージに属するクラスは、 RestControllerアノテーションが付与されていなければならない。 */
  @ArchTest
  static final ArchRule controller_package_classes_should_have_annotation =
      classes()
          .that()
          .resideInAPackage("..presentation.controller..")
          .and()
          .areNotEnums()
          .should()
          .beAnnotatedWith(RestController.class);

  /** ControllerのレスポンスはResponseEntityまたは特定のDTOを強制する */
  //  @ArchTest
  static final ArchRule controllers_should_return_response_entity_or_dto =
      methods()
          .that()
          .areDeclaredInClassesThat()
          .areAnnotatedWith(RestController.class)
          .and()
          .arePublic()
          .should()
          .haveRawReturnType(resideInAnyPackage("..dto..", "org.springframework.http.."));

  /** Controllerが直接Repository(Mapper)を呼び出すことを禁止する */
  @ArchTest
  static final ArchRule controllers_should_not_access_mappers_directly =
      noClasses()
          .that()
          .resideInAPackage("..presentation.controller..")
          .should()
          .accessClassesThat()
          .resideInAPackage("..infrastructure.persistence.mapper..");

  /** フィールド注入の禁止 (Constructor DI の強制) */
  @ArchTest
  static final ArchRule no_field_injection = noClasses().should().beAnnotatedWith(Autowired.class);
}
