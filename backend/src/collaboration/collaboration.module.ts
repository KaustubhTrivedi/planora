import { Module } from '@nestjs/common';
import { CollaborationService } from './collaboration.service';
import { CollaborationController } from './collaboration.controller';

@Module({
  controllers: [CollaborationController],
  providers: [CollaborationService],
})
export class CollaborationModule {}
