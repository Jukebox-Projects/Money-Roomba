export interface IAttachment {
  id?: number;
  url?: string;
  name?: string;
}

export class Attachment implements IAttachment {
  constructor(public id?: number, public url?: string, public name?: string) {}
}

export function getAttachmentIdentifier(attachment: IAttachment): number | undefined {
  return attachment.id;
}
