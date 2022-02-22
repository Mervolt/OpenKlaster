import {Component, OnInit, ViewChild} from '@angular/core';
import {AdminService} from '../../service/admin/admin.service';
import {AppComponent} from '../../app.component';
import {CookieService} from 'ngx-cookie-service';
import {UserDto} from '../../model/UserDto';
import {Router} from '@angular/router';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-user-management-list',
  templateUrl: './user-management-list.component.html',
  styleUrls: ['./user-management-list.component.css']
})
export class UserManagementListComponent implements OnInit {
  displayedColumns: string[] = ['username', 'email', 'role', 'active', 'action'];
  cookieService: CookieService;
  users: Array<UserDto>;
  currentUser: UserDto;
  showPasswordForm: boolean;
  hide: boolean;

  constructor(public adminService: AdminService,
              private router: Router,
              private appComp: AppComponent,
              private modalService: NgbModal) {
    this.cookieService = appComp.cookieService;
  }

  ngOnInit(): void {
    this.adminService.getUsers(this.cookieService).subscribe((value: UserDto[]) => {
      this.users = value;
    }, () => {
      this.appComp.cookieService.deleteAll();
      this.router.navigate(['/login']).then();
    });
  }

  open(content, user) {
    this.showPasswordForm = false;
    this.currentUser = user;
    this.hide = true;

    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title', size: 'sm'}).result.then(() => {
      this.adminService.putUser(this.currentUser, this.cookieService).subscribe(() => {
        this.ngOnInit();
      });
    },
      () => {});


  }

}
