import com.modulus.uno.menu.Menu
import com.modulus.uno.menu.MenuLink
import com.modulus.uno.Role

databaseChangeLog = {
  changeSet(author: "cggg88jorge (manual)", id: "adding-default-menus-for-user-m1") {
    grailsChange {
      change {
        Menu.withTransaction{
          def menuparentCorporation = new Menu(name:"Corporativos",internalUrl:"/")
          menuparentCorporation.save()
          def menuparentMenu = new Menu(name:"Operaciones del menú",internalUrl:"/")
          menuparentMenu.save()

          menuparentCorporation.addToMenus(new Menu(name:"Lista de Corporativos",internalUrl:"/dashboard/index"))
          menuparentCorporation.addToMenus(new Menu(name:"Crear Nuevo Corporativo",internalUrl:"/corporate/create"))

          menuparentCorporation.save()

          menuparentMenu.addToMenus(new Menu(name:"Lista de Operaciones del menú",internalUrl:"/menu/index"))
          menuparentMenu.addToMenus(new Menu(name:"Lista de Roles y sus menús",internalUrl:"/menuOperations/index"))

          menuparentMenu.save()
        }
      }
    }
  }

  changeSet(author: "cggg88jorge (manual)", id: "adding-relation-role-to-menu") {
    grailsChange {
      change {
        MenuLink.withTransaction{
          Menu menuCorp = Menu.findByName("Corporativos")
          Menu menuMenus = Menu.findByName("Operaciones del menú")

          Role roleM1 = Role.findByAuthority("ROLE_M1")

          new MenuLink(menu:menuCorp, menuRef:roleM1.id, type:"Role").save()
          new MenuLink(menu:menuMenus, menuRef:roleM1.id, type:"Role").save()
        }
      }
    }
  }
}

